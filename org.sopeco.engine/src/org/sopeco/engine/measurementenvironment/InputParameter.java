package org.sopeco.engine.measurementenvironment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for defining input / initialization or preparation parameters in mesearument environment
 * controllers.
 * 
 * @author Alexander Wert
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface InputParameter {
	
	public static final String DEFAULT = "DEFAULT";
	public static final String ROOT = "root";
	
	/**
	 * Specifies the name of the parameter.
	 */
	String name() default DEFAULT;

	/**
	 * Specifies the full qualified namespace this parameter belongs to.
	 */
	String namespace() default ROOT;
}
