package org.valdi.SuperApiX.common.annotation;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.valdi.SuperApiX.common.annotation.command.Command;
import org.valdi.SuperApiX.common.annotation.dependency.Dependency;
import org.valdi.SuperApiX.common.annotation.dependency.LoadBefore;
import org.valdi.SuperApiX.common.annotation.dependency.SoftDependency;
import org.valdi.SuperApiX.common.annotation.permission.ChildPermission;
import org.valdi.SuperApiX.common.annotation.permission.Permission;
import org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder;
import org.valdi.SuperApiX.common.annotation.plugin.LogPrefix;
import org.valdi.SuperApiX.common.annotation.plugin.Website;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

@SupportedAnnotationTypes("org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BukkitAnnotationProcessor extends AbstractProcessor {

    private boolean hasMainBeenFound = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Element mainPluginElement = null;
        hasMainBeenFound = false;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BukkitPlugin.class);
        if (elements.size() > 1) {
            AnnotationUtils.raiseError(processingEnv, "Found more than three plugin main class");
            return false;
        }

        if (elements.isEmpty()) {
            return false;
        }
        if (hasMainBeenFound) {
            AnnotationUtils.raiseError(processingEnv, "The plugin class has already been located, aborting!");
            return false;
        }
        mainPluginElement = elements.iterator().next();
        hasMainBeenFound = true;

        TypeElement mainPluginType;
        if (mainPluginElement instanceof TypeElement) {
            mainPluginType = (TypeElement) mainPluginElement;
        } else {
            AnnotationUtils.raiseError(processingEnv, "Element annotated with @BukkitPlugin is not a type!", mainPluginElement);
            return false;
        }

        if (!(mainPluginType.getEnclosingElement() instanceof PackageElement) && !mainPluginType.getModifiers().contains(Modifier.STATIC)) {
            AnnotationUtils.raiseError(processingEnv, "Element annotated with @BukkitPlugin is not top-level or static nested!", mainPluginType);
            return false;
        }

        if (!processingEnv.getTypeUtils().isSubtype(mainPluginType.asType(), AnnotationUtils.fromClass(processingEnv, JavaPlugin.class))) {
            AnnotationUtils.raiseError(processingEnv, "Class annotated with @BukkitPlugin is not an subclass of " + JavaPlugin.class.getName() + "!", mainPluginType);
            return false;
        }

        Map<String, Object> yml = new LinkedHashMap<>(); // linked so we can maintain the same output into file for sanity

        yml.put("platform", "Bukkit");

        // populate mainName
        final String mainName = mainPluginType.getQualifiedName().toString();
        yml.put("main", mainName); // always override this so we make sure the main class name is correct

        // populate plugin name
        AnnotationUtils.processAndPut(yml, "name", mainPluginType, mainName.substring(mainName.lastIndexOf('.') + 1), BukkitPlugin.class, String.class, "name");

        // populate version
        AnnotationUtils.processAndPut(yml, "version", mainPluginType, BukkitPlugin.DEFAULT_VERSION, BukkitPlugin.class, String.class, "version");

        // populate bukkit api
        BukkitPlugin.Target api = AnnotationUtils.process(mainPluginType, BukkitPlugin.Target.DEFAULT, BukkitPlugin.class, BukkitPlugin.Target.class, "api");
        if (api != BukkitPlugin.Target.DEFAULT) {
            yml.put("api-version", api.getVersion());
        }

        // populate plugin description
        AnnotationUtils.processAndPut(yml, "description", mainPluginType, null, Description.class, String.class);

        // populate plugin load order
        AnnotationUtils.processAndPut(yml, "load", mainPluginType, null, LoadOrder.class, String.class);

        // populate plugin authors
        Author[] authors = mainPluginType.getAnnotationsByType(Author.class);
        List<String> authorMap = new ArrayList<>();
        for (Author auth : authors) {
            authorMap.add(auth.value());
        }
        if (authorMap.size() > 1) {
            yml.put("authors", authorMap);
        } else if (authorMap.size() == 1) {
            yml.put("author", authorMap.iterator().next());
        }

        // populate website
        AnnotationUtils.processAndPut(yml, "website", mainPluginType, null, Website.class, String.class);

        // populate prefix
        AnnotationUtils.processAndPut(yml, "prefix", mainPluginType, null, LogPrefix.class, String.class);

        // populate dependencies
        Dependency[] hardDeps = mainPluginType.getAnnotationsByType(Dependency.class);
        List<String> hardDependencies = new ArrayList<>();
        for (Dependency hardDep : hardDeps) {
            hardDependencies.add(hardDep.value());
        }
        if (!hardDependencies.isEmpty()) {
            yml.put("depend", hardDependencies);
        }

        // populate soft-dependencies
        SoftDependency[] softDeps = mainPluginType.getAnnotationsByType(SoftDependency.class);
        List<String> softDependencies = new ArrayList<>();
        for (SoftDependency softDep : softDeps) {
            softDependencies.add(softDep.value());
        }
        if (!softDependencies.isEmpty()) {
            yml.put("softdepend", softDependencies);
        }

        // populate load-before
        LoadBefore[] loadBfs = mainPluginType.getAnnotationsByType(LoadBefore.class);
        List<String> loadBefore = new ArrayList<>();
        for (LoadBefore loadBf : loadBfs) {
            loadBefore.add(loadBf.value());
        }
        if (!loadBefore.isEmpty()) {
            yml.put("loadbefore", loadBefore);
        }

        // populate commands
        // Begin processing external command annotations
        Map<String, Map<String, Object>> commandMap = new LinkedHashMap<>();
        boolean result = processExternalCommands(processingEnv, roundEnv.getElementsAnnotatedWith(Command.class), mainPluginType, commandMap);
        if (!result) {
            // #processExternalCommand already raised the errors
            return false;
        }

        Command[] commands = mainPluginType.getAnnotationsByType(Command.class);

        // Check main class for any command annotations
        if (commands != null) {
            Map<String, Map<String, Object>> merged = new LinkedHashMap<>();
            merged.putAll(commandMap);
            merged.putAll(processCommands(commands));
            commandMap = merged;
        }

        yml.put("commands", commandMap);

        // populate permissions
        Map<String, Map<String, Object>> permissionMetadata = new LinkedHashMap<>();

        Set<? extends Element> permissionAnnotations = roundEnv.getElementsAnnotatedWith(Command.class);
        if (permissionAnnotations.size() > 0) {
            for (Element element : permissionAnnotations) {
                if (element.equals(mainPluginType)) {
                    continue;
                }
                if (element.getAnnotation(Permission.class) != null) {
                    Permission permissionAnnotation = element.getAnnotation(Permission.class);
                    permissionMetadata.put(permissionAnnotation.name(), processPermission(permissionAnnotation));
                }
            }
        }

        Permission[] permissions = mainPluginType.getAnnotationsByType(Permission.class);
        if (permissions != null) {
            Map<String, Map<String, Object>> joined = new LinkedHashMap<>();
            joined.putAll(permissionMetadata);
            joined.putAll(processPermissions(permissions));
            permissionMetadata = joined;
        }
        yml.put("permissions", permissionMetadata);

        // Write to plugin.yml
        try {
            Yaml yaml = new Yaml();
            FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml");
            // try with resources will close the Writer since it implements Closeable
            try (Writer w = file.openWriter()) {
                w.append("# Auto-generated plugin.yml, generated at ")
                        .append(LocalDateTime.now().format(AnnotationUtils.dFormat))
                        .append(" by ")
                        .append(this.getClass().getName())
                        .append("\n\n");
                // have to format the yaml explicitly because otherwise it dumps child nodes as maps within braces.
                String raw = yaml.dumpAs(yml, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
                w.write(raw);
                w.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "NOTE: You are using " + this.getClass().getName() + ", an experimental API!");
        return true;
    }

    private boolean processExternalCommands(ProcessingEnvironment processingEnv, Set<? extends Element> commandExecutors,
                                            TypeElement mainPluginType, Map<String, Map<String, Object>> commandMetadata) {
        for (Element element : commandExecutors) {
            // Check to see if someone annotated a non-class with this
            if (!(element instanceof TypeElement)) {
                AnnotationUtils.raiseError(processingEnv, "Specified Command Executor class is not a class.");
                return false;
            }

            TypeElement typeElement = (TypeElement) element;
            if (typeElement.equals(mainPluginType)) {
                continue;
            }

            // Check to see if annotated class is actuall a command executor
            TypeMirror mirror = processingEnv.getElementUtils().getTypeElement(CommandExecutor.class.getName()).asType();
            if (!(processingEnv.getTypeUtils().isAssignable(typeElement.asType(), mirror))) {
                //AnnotationUtils.raiseError( processingEnv, "Specified Command Executor class is not assignable from CommandExecutor " );
                //return false;
                continue;
            }

            Command annotation = typeElement.getAnnotation(Command.class);
            commandMetadata.put(annotation.name(), this.processCommand(annotation));
        }
        return true;
    }

    /**
     * Processes a set of commands.
     *
     * @param commands The annotation.
     * @return The generated command metadata.
     */
    protected Map<String, Map<String, Object>> processCommands(Command[] commands) {
        Map<String, Map<String, Object>> commandList = new LinkedHashMap<>();
        for (Command command : commands) {
            commandList.put(command.name(), this.processCommand(command));
        }
        return commandList;
    }

    /**
     * Processes a single command.
     *
     * @param commandAnnotation The annotation.
     * @return The generated command metadata.
     */
    protected Map<String, Object> processCommand(Command commandAnnotation) {
        Map<String, Object> command = new LinkedHashMap<>();

        if (commandAnnotation.aliases().length == 1) {
            command.put("aliases", commandAnnotation.aliases()[0]);
        } else if (commandAnnotation.aliases().length > 1) {
            command.put("aliases", commandAnnotation.aliases());
        }

        if (!"".equals(commandAnnotation.desc())) {
            command.put("description", commandAnnotation.desc());
        }
        if (!"".equals(commandAnnotation.permission())) {
            command.put("permission", commandAnnotation.permission());
        }
        if (!"".equals(commandAnnotation.permissionMessage())) {
            command.put("permission-message", commandAnnotation.permissionMessage());
        }
        if (!"".equals(commandAnnotation.usage())) {
            command.put("usage", commandAnnotation.usage());
        }

        return command;
    }

    /**
     * Processes a set of permissions.
     *
     * @param permissions The annotation.
     * @return The generated permission metadata.
     */
    protected Map<String, Map<String, Object>> processPermissions(Permission[] permissions) {
        Map<String, Map<String, Object>> permissionList = new LinkedHashMap<>();
        for (Permission permission : permissions) {
            permissionList.put(permission.name(), this.processPermission(permission));
        }
        return permissionList;
    }

    /**
     * Processes a command.
     *
     * @param permissionAnnotation The annotation.
     * @return The generated permission metadata.
     */
    protected Map<String, Object> processPermission(Permission permissionAnnotation) {
        Map<String, Object> permission = new LinkedHashMap<>();

        if (!permissionAnnotation.desc().isEmpty()) {
            permission.put("description", permissionAnnotation.desc());
        }
        permission.put("default", permissionAnnotation.defaultValue().toString());

        if (permissionAnnotation.children().length > 0) {
            Map<String, Boolean> childrenList = new LinkedHashMap<>(); // maintain order
            for (ChildPermission childPermission : permissionAnnotation.children()) {
                childrenList.put(childPermission.name(), childPermission.inherit());
            }
            permission.put("children", childrenList);
        }

        return permission;
    }

}
