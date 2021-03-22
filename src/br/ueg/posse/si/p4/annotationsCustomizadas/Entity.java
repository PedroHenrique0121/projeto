/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ueg.posse.si.p4.annotationsCustomizadas;

import br.ueg.posse.si.p4.db.ConnectionFactory;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;

/**
 *
 * @author pedro
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface Entity  {
        
   
}
