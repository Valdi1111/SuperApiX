package org.valdi.SuperApiX.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class AnnotationUtils {

    public static final DateTimeFormatter dFormat = DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss", Locale.ENGLISH );

    public static void raiseError(ProcessingEnvironment processingEnv, String message) {
        processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, message );
    }

    public static void raiseError(ProcessingEnvironment processingEnv, String message, Element element) {
        processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, message, element );
    }

    public static TypeMirror fromClass(ProcessingEnvironment processingEnv, Class<?> clazz) {
        return processingEnv.getElementUtils().getTypeElement( clazz.getName() ).asType();
    }

    public static <A extends Annotation, R> R processAndPut(Map<String, Object> map, String name, 
    		Element el, R defaultVal, Class<A> annotationType, Class<R> returnType) {
        return processAndPut( map, name, el, defaultVal, annotationType, returnType, "value" );
    }

    public static <A extends Annotation, R> R processAndPut(Map<String, Object> map, String name, 
    		Element el, R defaultVal, Class<A> annotationType, Class<R> returnType, String methodName) {
        R result = process( el, defaultVal, annotationType, returnType, methodName );
        if ( result != null )
            map.put( name, result );
        return result;
    }

    public static <A extends Annotation, R> R process(Element el, R defaultVal, Class<A> annotationType, Class<R> returnType, String methodName) {
        R result;
        A ann = el.getAnnotation( annotationType );
        if ( ann == null ) result = defaultVal;
        else {
            try {
                Method value = annotationType.getMethod( methodName );
                Object res = value.invoke( ann );
                result = ( R ) ( returnType == String.class ? res.toString() : returnType.cast( res ) );
            } catch ( Exception e ) {
                throw new RuntimeException( e ); // shouldn't happen in theory (blame Choco if it does)
            }
        }
        return result;
    }

}
