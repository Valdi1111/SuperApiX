package org.valdi.SuperApiX.common.annotation;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.valdi.SuperApiX.common.annotation.dependency.Dependency;
import org.valdi.SuperApiX.common.annotation.dependency.SoftDependency;
import org.valdi.SuperApiX.common.annotation.plugin.BungeePlugin;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.Website;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.plugin.Plugin;

@SupportedAnnotationTypes( "org.valdi.SuperApiX.common.annotation.plugin.BungeePlugin" )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public class BungeeAnnotationProcessor extends AbstractProcessor {
	
    private boolean hasMainBeenFound = false;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Element mainPluginElement = null;
        hasMainBeenFound = false;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith( BungeePlugin.class );
        if ( elements.size() > 1 ) {
        	AnnotationUtils.raiseError( processingEnv, "Found more than three plugin main class" );
            return false;
        }

        if ( elements.isEmpty() ) {
            return false;
        }
        if ( hasMainBeenFound ) {
        	AnnotationUtils.raiseError( processingEnv, "The plugin class has already been located, aborting!" );
            return false;
        }
        mainPluginElement = elements.iterator().next();
        hasMainBeenFound = true;

        TypeElement mainPluginType;
        if ( mainPluginElement instanceof TypeElement ) {
            mainPluginType = ( TypeElement ) mainPluginElement;
        } else {
        	AnnotationUtils.raiseError( processingEnv, "Element annotated with @Plugin is not a type!", mainPluginElement );
            return false;
        }

        if ( !( mainPluginType.getEnclosingElement() instanceof PackageElement ) && !mainPluginType.getModifiers().contains( Modifier.STATIC ) ) {
        	AnnotationUtils.raiseError( processingEnv, "Element annotated with @Plugin is not top-level or static nested!", mainPluginType );
            return false;
        }

        Map<String, Object> yml = Maps.newLinkedHashMap(); // linked so we can maintain the same output into file for sanity

        yml.put("platform", "Bungee");
	
	    // populate mainName
	    final String mainName = mainPluginType.getQualifiedName().toString();
	    yml.put( "main", mainName ); // always override this so we make sure the main class name is correct
	
	    // populate plugin name
	    AnnotationUtils.processAndPut( yml, "name", mainPluginType, mainName.substring( mainName.lastIndexOf( '.' ) + 1 ), BungeePlugin.class, String.class, "name" );
	
	    // populate version
	    AnnotationUtils.processAndPut( yml, "version", mainPluginType, BungeePlugin.DEFAULT_VERSION, BungeePlugin.class, String.class, "version" );
	
	    // populate plugin description
	    AnnotationUtils.processAndPut( yml, "description", mainPluginType, null, Description.class, String.class );
	    
	    if ( !processingEnv.getTypeUtils().isSubtype( mainPluginType.asType(), AnnotationUtils.fromClass( processingEnv, Plugin.class ) ) ) {
	    	AnnotationUtils.raiseError( processingEnv, "Class annotated with @Plugin is not an subclass of net.md_5.bungee.api.plugin.Plugin!", mainPluginType );
	    }
	
	    // author
        AnnotationUtils.processAndPut( yml, "author", mainPluginType, null, Author.class, String.class );
	
	    // dependencies
	    Dependency[] dependencies1 = mainPluginType.getAnnotationsByType( Dependency.class );
	    List<String> hardDependencies = Lists.newArrayList();
	    for ( Dependency dep : dependencies1 ) {
	        hardDependencies.add( dep.value() );
	    }
	    if ( !hardDependencies.isEmpty() ) yml.put( "depends", hardDependencies );
	
	    // soft-dependencies
	    SoftDependency[] dependencies2 = mainPluginType.getAnnotationsByType( SoftDependency.class );
	    List<String> softDependencies = Lists.newArrayList();
	    for ( SoftDependency dep : dependencies2 ) {
	    	softDependencies.add( dep.value() );
	    }
	    if ( !softDependencies.isEmpty() ) yml.put( "softdepends", softDependencies );
	
	    try {
	        Yaml yaml = new Yaml();
	        FileObject file = processingEnv.getFiler().createResource( StandardLocation.CLASS_OUTPUT, "", "bungee.yml" );
	        try ( Writer w = file.openWriter() ) {
	            w.append( "# Auto-generated bungee.yml, generated at " )
	             .append( LocalDateTime.now().format( AnnotationUtils.dFormat ) )
	             .append( " by " )
	             .append( this.getClass().getName() )
	             .append( "\n\n" );
	            // have to format the yaml explicitly because otherwise it dumps child nodes as maps within braces.
	            String raw = yaml.dumpAs( yml, Tag.MAP, DumperOptions.FlowStyle.BLOCK );
	            w.write( raw );
	            w.flush();
	            w.close();
	        }
	        // try with resources will close the Writer since it implements Closeable
	    } catch ( IOException e ) {
	        throw new RuntimeException( e );
	    }
	
	    processingEnv.getMessager().printMessage( Diagnostic.Kind.WARNING, "NOTE: You are using org.valdi.SuperApiX.common.annotation, an experimental API!" );
	    return false;
	}

}
