/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.data.mybatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * JPA注解处理工具类
 */
public class PersistentUtil {

	public static String getTableName(Class<?> clazz) {
		return getTableName(clazz, true);
	}

	/**
	 * 获取Table名称
	 * @param clazz claz
	 * @param camelToUnderline camelToUnderline
	 * @return string
	 */
	public static String getTableName(Class<?> clazz, boolean camelToUnderline) {
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			if (!"".equals(table.name().trim())) {
				return table.name();
			}
		}
		String className = clazz.getSimpleName();

		if (!camelToUnderline) {
			return className;
		}
		else {
			return NamingUtil.camelToUnderline(className);
		}
	}

	public static String getColumnName(Field field) {
		return getColumnName(field, true);
	}

	/**
	 * 获取列名，有Column注解时返回注解中定义的名称，没有注解时返回默认名称
	 * @param field field
	 * @param camelToUnderline camelToUnderline
	 * @return string
	 */
	public static String getColumnName(Field field, boolean camelToUnderline) {
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (!"".equals(column.name().trim())) {
				return column.name().toUpperCase();
			}
		}
		if (!camelToUnderline) {
			return field.getName();
		}
		else {
			return NamingUtil.camelToUnderline(field.getName());
		}
	}

	public static String getMappedName(Field field) {
		if (field.isAnnotationPresent(OneToOne.class)) {
			OneToOne one = field.getAnnotation(OneToOne.class);
			if (!one.mappedBy().trim().equals("")) {
				return one.mappedBy();
			}
		}
		if (field.isAnnotationPresent(OneToMany.class)) {
			OneToMany one = field.getAnnotation(OneToMany.class);
			if (!one.mappedBy().trim().equals("")) {
				return one.mappedBy();
			}
		}
		return null;
	}

	public static List<Field> getPersistentFields(Class<?> clazz) {
		List<Field> list = new ArrayList<>();
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (isPersistentField(field)) {
					list.add(field);
				}
			}
			searchType = searchType.getSuperclass();
		}
		return list;
	}

	public static Field getIdField(Class<?> clazz) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Id.class) && isPersistentField(field)) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static boolean insertable(Field field) {
		if (!isPersistentField(field) || isAssociationField(field)) {
			return false;
		}

		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			return column.insertable();
		}

		return true;
	}

	public static boolean updatable(Field field) {
		if (!isPersistentField(field) || isAssociationField(field)) {
			return false;
		}

		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			return column.updatable();
		}

		return true;
	}

	public static boolean isPersistentField(Field field) {
		return !Modifier.isStatic(field.getModifiers()) &&
				!field.isAnnotationPresent(Transient.class);
	}

	public static boolean isAssociationField(Field field) {
		return field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(OneToMany.class);
	}
}
