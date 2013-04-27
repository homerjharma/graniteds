/*
  GRANITE DATA SERVICES
  Copyright (C) 2013 GRANITE DATA SERVICES S.A.S.

  This file is part of Granite Data Services.

  Granite Data Services is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published by
  the Free Software Foundation; either version 2 of the License, or (at your
  option) any later version.

  Granite Data Services is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License
  for more details.

  You should have received a copy of the GNU Library General Public License
  along with this library; if not, see <http://www.gnu.org/licenses/>.
*/

package org.granite.messaging.jmf.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * @author Franck WOLFF
 */
public class PersistentMap<K, V> extends AbstractPersistentMapCollection<K, V, Map<K, V>> implements Map<K, V> {

	public PersistentMap() {
	}

	public PersistentMap(Map<K, V> collection) {		
		this(collection, true);
	}

	public PersistentMap(Map<K, V> collection, boolean clone) {	
		if (collection instanceof SortedSet)
			throw new IllegalArgumentException("Should not be a SortedSet: " + collection);
		
		if (collection != null)
			init(clone ? new HashMap<K, V>(collection) : collection, false);
	}

	@Override
	protected PersistentCollectionSnapshot newSnapshot(boolean blank) {
		if (blank || !wasInitialized())
			return new PersistentCollectionSnapshot();
		return new PersistentCollectionSnapshot(getMapElements(), isDirty());
	}

	@Override
	protected Map<K, V> newCollection(PersistentCollectionSnapshot snapshot) {
		return new HashMap<K, V>();
	}
}