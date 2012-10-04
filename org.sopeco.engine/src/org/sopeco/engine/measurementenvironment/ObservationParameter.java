package org.sopeco.engine.measurementenvironment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for defining observation parameters in mesearument
 * environment controllers. Note, parameter types annotated by this Annotation
 * should be of type ParameterValueList<?>! For these attributes no instances
 * have to be created manually. The instances of the ParameterValueLists will be
 * created automatically during creation of the MEController.
 * 
 * @author Alexander Wert
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ObservationParameter {
	/**
	 * Specifies the name of the parameter.
	 */
	String name() default "DEFAULT";

	/**
	 * Specifies the full qualified namespace this parameter belongs to.
	 */
	String namespace() default "root";
}
