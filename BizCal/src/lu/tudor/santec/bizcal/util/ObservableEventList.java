/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu  
 *  
 * This library is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License (version 2.1)
 * as published by the Free Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but     
 * WITHOUT ANY WARRANTY; without even the implied warranty of               
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU        
 * Lesser General Public License for more details.                          
 * 
 * You should have received a copy of the GNU Lesser General Public         
 * License along with this library; if not, write to the Free Software      
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *******************************************************************************/
/**
 * @author Martin Heinemann martin.heinemann@tudor.lu
 *
 *
 *
 * @version
 * <br>$Log: ObservableEventList.java,v $
 * <br>Revision 1.3  2008/08/12 12:47:28  heine_
 * <br>fixed some bugs and made code improvements
 * <br>
 * <br>Revision 1.2  2007/09/20 07:23:16  heine_
 * <br>new version commit
 * <br>
 * <br>Revision 1.4  2007/06/20 12:08:24  heinemann
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.3  2007/06/14 13:31:25  heinemann
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.2  2007/06/04 11:36:42  heinemann
 * <br>first try of synchronisation
 * <br>
 * <br>Revision 1.1  2007/05/25 13:43:59  heinemann
 * <br>pres-weekend checkin
 * <br>
 *
 */
package lu.tudor.santec.bizcal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import bizcal.common.Event;

/**
 * Observable ArrayList for the EventModel
 * 
 * @author martin.heinemann@tudor.lu
 *         22.05.2007
 *         14:06:12
 * 
 * 
 * @version
 * <br>
 *          $Log: ObservableEventList.java,v $ <br>
 *          Revision 1.3 2008/08/12 12:47:28 heine_ <br>
 *          fixed some bugs and made code improvements <br>
 * <br>
 *          Revision 1.2 2007/09/20 07:23:16 heine_ <br>
 *          new version commit <br>
 * <br>
 *          Revision 1.4 2007/06/20 12:08:24 heinemann <br>
 *          *** empty log message *** <br>
 * <br>
 *          Revision 1.3 2007/06/14 13:31:25 heinemann <br>
 *          *** empty log message *** <br>
 * <br>
 *          Revision 1.2 2007/06/04 11:36:42 heinemann <br>
 *          first try of synchronisation <br>
 * <br>
 *          Revision 1.1 2007/05/25 13:43:59 heinemann <br>
 *          pres-weekend checkin <br>
 * 
 */
public class ObservableEventList extends Observable implements List<Event> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Event> list = new ArrayList<Event>();
	
	private boolean notifyEnabled = true;
	
	@Override
	public synchronized boolean add(Event o) {
		/* ====================================================== */
		boolean b = this.list.add(o);
		this.setChanged();
		this.notifyObservers();
		return b;
		/* ====================================================== */
	}
	
	@Override
	public synchronized void add(int index, Event element) {
		/* ====================================================== */
		this.list.add(index, element);
		this.setChanged();
		this.notifyObservers();
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean addAll(Collection<? extends Event> c) {
		/* ====================================================== */
		boolean b = this.list.addAll(c);
		this.setChanged();
		this.notifyObservers();
		
		return b;
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean addAll(int index, Collection<? extends Event> c) {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		return this.list.addAll(index, c);
		/* ====================================================== */
	}
	
	@Override
	public synchronized void clear() {
		/* ====================================================== */
		this.setChanged();
		this.list.clear();
		this.notifyObservers();
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean contains(Object o) {
		/* ====================================================== */
		return this.list.contains(o);
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		/* ====================================================== */
		return this.list.containsAll(c);
		/* ====================================================== */
	}
	
	@Override
	public synchronized Event get(int index) {
		/* ====================================================== */
		return this.list.get(index);
		/* ====================================================== */
	}
	
	@Override
	public synchronized int indexOf(Object o) {
		/* ====================================================== */
		return this.list.indexOf(o);
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean isEmpty() {
		/* ====================================================== */
		return this.list.isEmpty();
		/* ====================================================== */
	}
	
	@Override
	public synchronized Iterator<Event> iterator() {
		/* ====================================================== */
		return this.list.iterator();
		/* ====================================================== */
	}
	
	@Override
	public synchronized int lastIndexOf(Object o) {
		/* ====================================================== */
		return this.list.lastIndexOf(o);
		/* ====================================================== */
	}
	
	@Override
	public synchronized ListIterator<Event> listIterator() {
		/* ====================================================== */
		return this.list.listIterator();
		/* ====================================================== */
	}
	
	@Override
	public synchronized ListIterator<Event> listIterator(int index) {
		/* ====================================================== */
		return this.list.listIterator(index);
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean remove(Object o) {
		/* ====================================================== */
		if (this.notifyEnabled) {
			this.setChanged();
			this.notifyObservers();
		}
		return this.list.remove(o);
		/* ====================================================== */
	}
	
	@Override
	public synchronized Event remove(int index) {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		return this.list.remove(index);
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		return this.list.removeAll(c);
		/* ====================================================== */
	}
	
	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		return this.list.retainAll(c);
		/* ====================================================== */
	}
	
	@Override
	public synchronized Event set(int index, Event element) {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		return this.list.set(index, element);
		/* ====================================================== */
	}
	
	@Override
	public synchronized int size() {
		/* ====================================================== */
		return this.list.size();
		/* ====================================================== */
	}
	
	@Override
	public synchronized List<Event> subList(int fromIndex, int toIndex) {
		/* ====================================================== */
		return this.list.subList(fromIndex, toIndex);
		/* ====================================================== */
	}
	
	@Override
	public synchronized Object[] toArray() {
		/* ====================================================== */
		return this.list.toArray();
		/* ====================================================== */
	}
	
	@Override
	public synchronized <T> T[] toArray(T[] a) {
		/* ====================================================== */
		return this.list.toArray(a);
		/* ====================================================== */
	}
	
	public synchronized void trigger() {
		/* ====================================================== */
		this.setChanged();
		this.notifyObservers();
		/* ====================================================== */
	}
	
	public synchronized void setEnableNotify(boolean b) {
		/* ================================================== */
		this.notifyEnabled = b;
		/* ================================================== */
	}
	
}
