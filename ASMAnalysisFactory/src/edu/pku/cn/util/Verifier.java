/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-26
 * @modifier: liuxi
 * @time 2010-3-26
 * @reviewer: liuxi
 * @time 2010-3-26
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.hierarchy.Repository;

/**
 * @author liuxi
 */
public class Verifier implements HA {
	URLClassLoader cl;
	public Verifier(){
	}
	private void getClassLoader(){
		CodaProperties coda=CodaProperties.e();
		URL[] urls   =new URL[coda.libPath.size()+1];
		try {
			urls[0]=new URL("file:///"+coda.getProjectPath());
			for(int i=1;i<urls.length;i++){
				urls[i]=new URL("file:///"+coda.libPath.get(i-1));
			}
			cl=new URLClassLoader(urls,this.getClass().getClassLoader());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 public boolean isInterface(final Type t) {
     Class c = getClass(t);
     if(c==null)
     	return false;
     return getClass(t).isInterface();
 }

 protected Type getSuperClass(final Type t) {
     Class c = getClass(t);
     if(c==null)
     	return null;
     c=c.getSuperclass();
     return c == null ? null : Type.getType(c);
 }

 public boolean isAssignableFrom(final Type t, final Type u) {
     if (t.equals(u)) {
         return true;
     }
     Class c=getClass(t);
     if(c==null)
     	return false;
     Class uc=getClass(u);
     if(uc==null)
    	 return false;
     return c.isAssignableFrom(uc);
 }
 protected Class getClass(final Type t) {
 	if(cl==null)
 		getClassLoader();
     try {
         if (t.getSort() == Type.ARRAY) {
             return Class.forName(t.getDescriptor().replace(
                 '/', '.'), true, cl);
         }
         return cl.loadClass(t.getClassName());
     } catch (Exception e) {
     	//System.err.println(e.toString());
     	return null;
         //throw new RuntimeException(e.toString()+" " +cl, e);
     }catch(NoClassDefFoundError e){
//     	System.err.println(e.toString());
    	return null;
    }
 }
@Override
public boolean isInstanceOf(Type t, Type u) {
    if (t.equals(u)) {
        return true;
    }
    Class c=getClass(t);
    if(c==null)
    	return false;
	return isAssignableFrom(u, t);
}
}

class RepositoryHA implements HA{
	Repository repo=Repository.getInstance();
	@Override
	public boolean isAssignableFrom(Type t, Type u) {
		// TODO Auto-generated method stub
		if(repo.instanceOf(t.getClassName(), u.getClassName()))
			return true;
		if(repo.implementOf(t.getClassName(), u.getClassName()))
			return true;
		return false;
	}

	@Override
	public boolean isInstanceOf(Type t, Type u) {
		// TODO Auto-generated method stub
		return repo.instanceOf(t.getClassName(), u.getClassName());
	}

	@Override
	public boolean isInterface(Type t) {
		// TODO Auto-generated method stub
		ClassNode cn=repo.findClassNode(t.getClassName());
		if(cn!=null)
			return cn.isInterface();
		return false;
	}
	
}
// end
