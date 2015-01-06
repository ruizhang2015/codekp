/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Jan 6, 2015
 * @modifier: ZR-Private
 * @time Jan 6, 2015
 * @reviewer: ZR-Private
 * @time Jan 6, 2015
 * (C) Copyright PKU Software Lab. 2015
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;

public class ListParams extends Doclet {

    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc cd = classes[i];
            //printMembers(cd.constructors());
            printMembers(cd.methods());
        }
        return true;
    }

    static void printMembers(ExecutableMemberDoc[] mems) {
        for (int i = 0; i < mems.length; ++i) {
        	//mems[i].
            //ParamTag[] params = mems[i].paramTags();
            System.out.println("[mname]" + mems[i].qualifiedName());
            System.out.println("[mlineno]" + mems[i].position());
            System.out.println("[mcomments]" + mems[i].getRawCommentText());

            
            /*for (int j = 0; j < params.length; ++j) {
                System.out.println("   " + params[j].parameterName()
                    + " - " + params[j].parameterComment() + " CC " + params[j].text());
            }*/
        }
    }        
}

// end
