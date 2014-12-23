/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages.impl;

import java.util.ArrayList;
import java.util.List;

import edu.pku.cn.xml.messages.BugCategory;
import edu.pku.cn.xml.messages.MessageCollectionType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Message Collection Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link edu.pku.cn.xml.messages.impl.MessageCollectionTypeImpl#getBugCategory
 * <em>Bug Category</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MessageCollectionTypeImpl implements MessageCollectionType {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getBugCategory() <em>Bug Category</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBugCategory()
	 * @generated
	 * @ordered
	 */
	protected List<BugCategory> bugCategory = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MessageCollectionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List<BugCategory> getBugCategory() {
		if (bugCategory == null) {
			bugCategory = new ArrayList<BugCategory>();
		}
		return bugCategory;
	}

} // MessageCollectionTypeImpl