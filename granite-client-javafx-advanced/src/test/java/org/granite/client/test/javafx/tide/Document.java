/**
 *   GRANITE DATA SERVICES
 *   Copyright (C) 2006-2014 GRANITE DATA SERVICES S.A.S.
 *
 *   This file is part of the Granite Data Services Platform.
 *
 *                               ***
 *
 *   Community License: GPL 3.0
 *
 *   This file is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation, either version 3 of the License,
 *   or (at your option) any later version.
 *
 *   This file is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *                               ***
 *
 *   Available Commercial License: GraniteDS SLA 1.0
 *
 *   This is the appropriate option if you are creating proprietary
 *   applications and you are not prepared to distribute and share the
 *   source code of your application under the GPL v3 license.
 *
 *   Please visit http://www.granitedataservices.com/license for more
 *   details.
 */
/**
 * Generated by Gas3 v1.1.0 (Granite Data Services) on Sat Jul 26 17:58:20 CEST 2008.
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (Person.as).
 */

package org.granite.client.test.javafx.tide;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableSet;

import org.granite.client.javafx.persistence.collection.FXPersistentCollections;
import org.granite.client.messaging.RemoteAlias;
import org.granite.client.persistence.Entity;
import org.granite.client.persistence.Lazy;

@Entity
@RemoteAlias("org.granite.test.tide.Document")
public class Document extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
    @Lazy
    private ReadOnlySetWrapper<DocumentList> documentLists = FXPersistentCollections.readOnlyObservablePersistentSet(this, "documentLists");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<DocumentPayload> payload = new SimpleObjectProperty<DocumentPayload>(this, "payload");


    public Document() {
        super();
    }

    public Document(Long id, Long version, String uid, String name) {
        super(id, version, uid);
        this.name.set(name);
    }

    public Document(Long id, boolean initialized, String detachedState) {
        super(id, initialized, detachedState);
    }

    public StringProperty nameProperty() {
        return name;
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public ReadOnlySetProperty<DocumentList> documentListsProperty() {
        return documentLists.getReadOnlyProperty();
    }
    public ObservableSet<DocumentList> getDocumentLists() {
        return documentLists.get();
    }
    
    public ObjectProperty<DocumentPayload> payloadProperty() {
    	return payload;
    }
    public DocumentPayload getPayload() {
    	return payload.get();
    }
    public void setPayload(DocumentPayload payload) {
    	this.payload.set(payload);
    }
}
