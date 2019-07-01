package com.saikrupa.app.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.saikrupa.app.dto.ContactPerson;

public class ContactPersonListModel extends DefaultListModel<ContactPerson> {

	
	@Override
	public void addElement(ContactPerson contact) {
		super.addElement(contact);
		getContactList().add(contact);
	}

	@Override
	public void add(int index, ContactPerson contact) {		
		super.add(index, contact);
		contactList.add(index, contact);		
	}

	@Override
	public void removeElementAt(int index) {
		ContactPerson cp = getElementAt(index);
		contactList.remove(cp);
		super.removeElementAt(index);
	}

	private static final long serialVersionUID = 1L;
	
	private List<ContactPerson> contactList;

	public List<ContactPerson> getContactList() {
		return contactList;
	}

	public ContactPersonListModel() {
		super();
		contactList = new ArrayList<ContactPerson>();
	}	


	public void setContactList(List<ContactPerson> contactList) { 
		for(ContactPerson p : contactList) {
			addElement(p);
		}
	}

}
