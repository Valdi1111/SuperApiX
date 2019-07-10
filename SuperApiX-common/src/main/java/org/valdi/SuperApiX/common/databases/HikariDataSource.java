package org.valdi.SuperApiX.common.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariPoolMXBean;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.pool.HikariPool.PoolInitializationException;
import org.valdi.SuperApiX.common.logging.SuperLogger;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zaxxer.hikari.pool.HikariPool.POOL_NORMAL;

/**
 * A custom HikariCP pooled DataSource.
 *
 * @author david
 */
public class HikariDataSource extends HikariConfig implements DataSource, Closeable
{
    private final AtomicBoolean isShutdown = new AtomicBoolean();

    private final SuperLogger logger;
    private final HikariPool fastPathPool;
    private volatile HikariPool pool;

    private static Method seal;

    static {
        try {
            seal = HikariConfig.class.getDeclaredMethod("seal");
            seal.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Default constructor.  Setters are used to configure the pool.  Using
     * this constructor vs. {@link #HikariDataSource(SuperLogger, HikariConfig)} will
     * result in {@link #getConnection()} performance that is slightly lower
     * due to lazy initialization checks.
     *
     * The first call to {@link #getConnection()} starts the pool.  Once the pool
     * is started, the configuration is "sealed" and no further configuration
     * changes are possible -- except via {@link HikariConfigMXBean} methods.
     */
    public HikariDataSource(SuperLogger logger)
    {
        super();
        this.logger = logger;
        this.fastPathPool = null;
    }

    /**
     * Construct a HikariDataSource with the specified configuration.  The
     * {@link HikariConfig} is copied and the pool is started by invoking this
     * constructor.
     *
     * The {@link HikariConfig} can be modified without affecting the HikariDataSource
     * and used to initialize another HikariDataSource instance.
     *
     * @param configuration a HikariConfig instance
     */
    public HikariDataSource(SuperLogger logger, HikariConfig configuration)
    {
        this.logger = logger;
        configuration.validate();
        configuration.copyStateTo(this);

        logger.info(configuration.getPoolName()+ " - Starting...");
        this.pool = fastPathPool = new HikariPool(this);
        logger.info(configuration.getPoolName() + " - Start completed.");

        //this.seal();
        try {
            seal.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // ***********************************************************************
    //                          DataSource methods
    // ***********************************************************************

    /** {@inheritDoc} */
    @Override
    public Connection getConnection() throws SQLException
    {
        if (isClosed()) {
            throw new SQLException("HikariDataSource " + this + " has been closed.");
        }

        if (fastPathPool != null) {
            return fastPathPool.getConnection();
        }

        // See http://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
        HikariPool result = pool;
        if (result == null) {
            synchronized (this) {
                result = pool;
                if (result == null) {
                    validate();
                    logger.info(getPoolName()+ " - Starting...");
                    try {
                        pool = result = new HikariPool(this);
                        //this.seal();
                        try {
                            seal.invoke(this);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    catch (PoolInitializationException pie) {
                        if (pie.getCause() instanceof SQLException) {
                            throw (SQLException) pie.getCause();
                        }
                        else {
                            throw pie;
                        }
                    }
                    logger.info(getPoolName()+ " - Start completed.");
                }
            }
        }

        return result.getConnection();
    }

    /** {@inheritDoc} */
    @Override
    public Connection getConnection(String username, String password) throws SQLException
    {
        throw new SQLFeatureNotSupportedException();
    }

    /** {@inheritDoc} */
    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        HikariPool p = pool;
        return (p != null ? p.getUnwrappedDataSource().getLogWriter() : null);
    }

    /** {@inheritDoc} */
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException
    {
        HikariPool p = pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLogWriter(out);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setLoginTimeout(int seconds) throws SQLException
    {
        HikariPool p = pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLoginTimeout(seconds);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getLoginTimeout() throws SQLException
    {
        HikariPool p = pool;
        return (p != null ? p.getUnwrappedDataSource().getLoginTimeout() : 0);
    }

    /** {@inheritDoc} */
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        if (iface.isInstance(this)) {
            return (T) this;
        }

        HikariPool p = pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return (T) unwrappedDataSource;
            }

            if (unwrappedDataSource != null) {
                return unwrappedDataSource.unwrap(iface);
            }
        }

        throw new SQLException("Wrapped DataSource is not an instance of " + iface);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        if (iface.isInstance(this)) {
            return true;
        }

        HikariPool p = pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return true;
            }

            if (unwrappedDataSource != null) {
                return unwrappedDataSource.isWrapperFor(iface);
            }
        }

        return false;
    }

    // ***********************************************************************
    //                        HikariConfigMXBean methods
    // ***********************************************************************

    /** {@inheritDoc} */
    @Override
    public void setMetricRegistry(Object metricRegistry)
    {
        boolean isAlreadySet = getMetricRegistry() != null;
        super.setMetricRegistry(metricRegistry);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            }
            else {
                p.setMetricRegistry(super.getMetricRegistry());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory)
    {
        boolean isAlreadySet = getMetricsTrackerFactory() != null;
        super.setMetricsTrackerFactory(metricsTrackerFactory);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            }
            else {
                p.setMetricsTrackerFactory(super.getMetricsTrackerFactory());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setHealthCheckRegistry(Object healthCheckRegistry)
    {
        boolean isAlreadySet = getHealthCheckRegistry() != null;
        super.setHealthCheckRegistry(healthCheckRegistry);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            }
            else {
                p.setHealthCheckRegistry(super.getHealthCheckRegistry());
            }
        }
    }

    // ***********************************************************************
    //                        HikariCP-specific methods
    // ***********************************************************************

    /**
     * Returns {@code true} if the pool as been started and is not suspended or shutdown.
     *
     * @return {@code true} if the pool as been started and is not suspended or shutdown.
     */
    public boolean isRunning()
    {
        return pool != null && pool.poolState == POOL_NORMAL;
    }

    /**
     * Get the {@code HikariPoolMXBean} for this HikariDataSource instance.  If this method is called on
     * a {@code HikariDataSource} that has been constructed without a {@code HikariConfig} instance,
     * and before an initial call to {@code #getConnection()}, the return value will be {@code null}.
     *
     * @return the {@code HikariPoolMXBean} instance, or {@code null}.
     */
    public HikariPoolMXBean getHikariPoolMXBean()
    {
        return pool;
    }

    /**
     * Get the {@code HikariConfigMXBean} for this HikariDataSource instance.
     *
     * @return the {@code HikariConfigMXBean} instance.
     */
    public HikariConfigMXBean getHikariConfigMXBean()
    {
        return this;
    }

    /**
     * Evict a connection from the pool.  If the connection has already been closed (returned to the pool)
     * this may result in a "soft" eviction; the connection will be evicted sometime in the future if it is
     * currently in use.  If the connection has not been closed, the eviction is immediate.
     *
     * @param connection the connection to evict from the pool
     */
    public void evictConnection(Connection connection)
    {
        HikariPool p;
        if (!isClosed() && (p = pool) != null && connection.getClass().getName().startsWith("com.zaxxer.hikari")) {
            p.evictConnection(connection);
        }
    }

    /**
     * Shutdown the DataSource and its associated pool.
     */
    @Override
    public void close()
    {
        if (isShutdown.getAndSet(true)) {
            return;
        }

        HikariPool p = pool;
        if (p != null) {
            try {
                logger.info(getPoolName() + " - Shutdown initiated...");
                p.shutdown();
                logger.info(getPoolName() + " - Shutdown completed.");
            }
            catch (InterruptedException e) {
                logger.warning(getPoolName()+ " - Interrupted during closing", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Determine whether the HikariDataSource has been closed.
     *
     * @return true if the HikariDataSource has been closed, false otherwise
     */
    public boolean isClosed()
    {
        return isShutdown.get();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "HikariDataSource (" + pool + ")";
    }
}