/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-25
 * @modifier: liuxi
 * @time 2010-3-25
 * @reviewer: liuxi
 * @time 2010-3-25
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.lang.instrument.ClassDefinition;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.util.CodaProperties;

/**
 * @author liuxi
 */
public class CFGVerifix extends SimpleVerifier{
	URLClassLoader cl;
	ClassNodeLoader loader;
	   /**
     * The class that is verified.
     */
    private final Type currentClass;

    /**
     * The super class of the class that is verified.
     */
    private final Type currentSuperClass;

    /**
     * The interfaces implemented by the class that is verified.
     */
    private final List currentClassInterfaces;

    /**
     * If the class that is verified is an interface.
     */
    private final boolean isInterface;
	public CFGVerifix(){
        this.currentClass = null;
        this.currentSuperClass = null;
        this.currentClassInterfaces = null;
        this.isInterface = false;
	}
	private void getClassLoader(){
		CodaProperties coda=CodaProperties.e();
		URL[] urls   =new URL[coda.libPath.size()+1];
		try {
			urls[0]=new URL("file:///"+coda.getProjectPath());
			//loader=new ClassNodeLoader(coda.projectPath);
			for(int i=1;i<urls.length;i++){
				urls[i]=new URL("file:///"+coda.libPath.get(i-1));
			}
			cl=new URLClassLoader(urls,this.getClass().getClassLoader());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    protected boolean isInterface(final Type t) {
        if (currentClass != null && t.equals(currentClass)) {
            return isInterface;
        }
        Class c = getClass(t);
        if(c==null)
        	return false;
        return getClass(t).isInterface();
    }

    protected Type getSuperClass(final Type t) {
        if (currentClass != null && t.equals(currentClass)) {
            return currentSuperClass;
        }
        Class c = getClass(t);
        if(c==null)
        	return null;
        c=c.getSuperclass();
        return c == null ? null : Type.getType(c);
    }

    protected boolean isAssignableFrom(final Type t, final Type u) {
    	if (t == null) {
    		return true;
    	}
    	if (t.equals(u)) {
            return true;
        }
        if (currentClass != null && t.equals(currentClass)) {
            if (getSuperClass(u) == null) {
                return false;
            } else {
                return isAssignableFrom(t, getSuperClass(u));
            }
        }
        if (currentClass != null && u.equals(currentClass)) {
            if (isAssignableFrom(t, currentSuperClass)) {
                return true;
            }
            if (currentClassInterfaces != null) {
                for (int i = 0; i < currentClassInterfaces.size(); ++i) {
                    Type v = (Type) currentClassInterfaces.get(i);
                    if (isAssignableFrom(t, v)) {
                        return true;
                    }
                }
            }
            return false;
        }
        Class c=getClass(t);
        if(c==null)
        	return false;
        Class uc=getClass(u);
        if(uc==null)
        	return true;
        return c.isAssignableFrom(uc);
//        return c.isAssignableFrom(getClass(u));
    }
    protected Class getClass(final Type t) {
//    	System.out.println(t.getClassName());
    	
    	if(cl==null)
    		getClassLoader();
        try {
//        	cl.loadClass("org.jboss.remoting.Lease$LeaseTimerTask");
            if (t.getSort() == Type.ARRAY) {
//            	return cl.loadClass(t.getDescriptor().replace(
//                        '/', '.'));
//            	String className=t.getDescriptor();
//            	className=className.substring(t.getDimensions()).replace('/', '.');
//            	Type tt=Type.getType(className);
//            	if(tt.getSort()>Type.ARRAY)
//            	cl.loadClass(tt.getClassName());
                return Class.forName(t.getDescriptor().replace(
                    '/', '.'), true, cl);
            }
            return cl.loadClass(t.getClassName());
//            return Class.forName(t.getClassName(),false,cl);
        } catch (Exception e) {
        	System.err.println(e.toString());
        	return null;
            //throw new RuntimeException(e.toString()+" " +cl, e);
        }catch(Error e){
        	System.err.println(t.getClassName()+e.toString());
        	return null;
        }
    }
}

// end
