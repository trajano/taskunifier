package com.leclercb.taskunifier.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TagList implements Cloneable, Serializable, Iterable<Tag> {
	
	private List<Tag> tags;
	
	public TagList() {
		this.tags = new ArrayList<Tag>();
	}
	
	@Override
	protected TagList clone() {
		TagList list = new TagList();
		list.addTags(this);
		return list;
	}
	
	@Override
	public Iterator<Tag> iterator() {
		return this.tags.iterator();
	}
	
	public List<Tag> asList() {
		return new ArrayList<Tag>(this.tags);
	}
	
	public int getIndexOf(Tag tag) {
		return this.tags.indexOf(tag);
	}
	
	public int getTagCount() {
		return this.tags.size();
	}
	
	public String getTag(int index) {
		return this.getTag(index);
	}
	
	public boolean containsTag(Tag tag) {
		return this.tags.contains(tag);
	}
	
	public boolean containsTag(String tag) {
		if (!Tag.isValid(tag))
			return false;
		
		return this.containsTag(new Tag(tag));
	}
	
	public boolean addTag(Tag tag) {
		if (tag == null)
			return false;
		
		if (this.tags.contains(tag))
			return false;
		
		this.tags.add(tag);
		return true;
	}
	
	public boolean addTag(String tag) {
		if (!Tag.isValid(tag))
			return false;
		
		return this.addTag(new Tag(tag));
	}
	
	public void addTags(TagList tags) {
		if (tags == null)
			return;
		
		for (Tag tag : tags) {
			this.addTag(tag);
		}
	}
	
	public void addTags(String[] tags) {
		if (tags == null)
			return;
		
		for (String tag : tags) {
			this.addTag(tag);
		}
	}
	
	public void addTags(Collection<Tag> tags) {
		if (tags == null)
			return;
		
		for (Tag tag : tags) {
			this.addTag(tag);
		}
	}
	
	public boolean replaceTag(Tag oldTag, Tag newTag) {
		if (oldTag == null || newTag == null)
			return false;
		
		if (!this.tags.contains(oldTag))
			return false;
		
		if (this.tags.contains(newTag))
			return false;
		
		this.tags.set(this.tags.indexOf(oldTag), newTag);
		return true;
	}
	
	public boolean removeTag(Tag tag) {
		return this.tags.remove(tag);
	}
	
	@Override
	public String toString() {
		return StringUtils.join(this.tags, ", ");
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof TagList) {
			TagList list = (TagList) o;
			
			return new EqualsBuilder().append(this.tags, list.tags).isEquals();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.tags);
		
		return hashCode.toHashCode();
	}
	
	public static TagList fromString(String string) {
		if (string == null)
			return new TagList();
		
		String[] tags = string.split(",");
		TagList list = new TagList();
		list.addTags(tags);
		
		return list;
	}
	
}
