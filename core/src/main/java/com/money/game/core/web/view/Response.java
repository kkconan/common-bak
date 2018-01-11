package com.money.game.core.web.view;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.money.game.core.exception.BaseException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Response extends HashMap<String, Object> {

	private static final long serialVersionUID = 5088494912966887333L;

	public Response() {
		this(true);
	}

	private Response(boolean init) {
		super();
		if (init) {
			this.init0();
		}
	}

	private void init0() {
		super.put(ERROR_CODE_KEY, 0);
		super.put(ERROR_MSG_KEY, "");
	}

	private static final String ERROR_CODE_KEY = "errorCode";
	private static final String ERROR_MSG_KEY = "errorMessage";

	public final static int VALID = 0;


	private static ParserConfig config = ParserConfig.getGlobalInstance();

	private static ConcurrentHashMap<Class<?>, FieldInfo[]> getters = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<Class<?>, Class<?>> alias = new ConcurrentHashMap<Class<?>, Class<?>>();

	public static Response error(Throwable error) {
		Response response = new Response();
		if (error instanceof BaseException) {
			BaseException me = (BaseException) error;
			response.put(ERROR_CODE_KEY, me.getCode());
			response.put(ERROR_MSG_KEY, me.getMessage());
		} else {
			response.put(ERROR_CODE_KEY, -1);
			response.put(ERROR_MSG_KEY, error.getMessage());
		}
		return response;
	}

	public Response with(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}
		Object val = this.parse(value);
		if (null == val) {
			return this;
		} else {
			super.put(key, val);
			return this;
		}
	}

	public Response with(Map<String, Object> attrs) {
		for (String key : attrs.keySet()) {
			this.with(key, attrs.get(key));
		}
		return this;
	}

	private static FieldInfo[] getFields(Class<?> clazz) {
		Class<?> safe = null;
		if (alias.containsKey(clazz)) {
			safe = alias.get(clazz);
		} else {
			safe = detect(clazz);
			alias.putIfAbsent(clazz, safe);
		}
		if (getters.containsKey(safe)) {
			return getters.get(safe);
		} else {
			List<FieldInfo> fields = TypeUtils.computeGetters(safe, null);
			fields.removeIf(fi -> fi.getField().isAnnotationPresent(Disview.class));
			FieldInfo[] fis = fields.toArray(new FieldInfo[0]);
			getters.putIfAbsent(safe, fis);
			return fis;
		}
	}

	private static boolean isproxy(Class<?> clazz) {
		boolean isproxy = false;
		if (clazz == org.hibernate.proxy.HibernateProxy.class || clazz == javassist.util.proxy.ProxyObject.class || clazz == javassist.util.proxy.Proxy.class) {
			isproxy = true;
		} else {
			Class<?>[] interfaces = clazz.getInterfaces();
			if (null != interfaces && interfaces.length > 0) {
				for (Class<?> i : interfaces) {
					isproxy = isproxy || isproxy(i);
				}
			}
		}
		return isproxy;
	}

	private static Class<?> detect(Class<?> clazz) {
		boolean isproxy = isproxy(clazz);
		if (isproxy) {
			Class<?> sp = clazz.getSuperclass();
			if (null != sp) {
				return clazz.getSuperclass();
			} else {
				return clazz;
			}
		} else {
			return clazz;
		}
	}

	private Object parse(Object value) {
		if (null == value) {
			return null;
		}

		if (value instanceof Response) {
			return value;
		} else if (value instanceof Viewable) {
			Viewable view = (Viewable) value;
			return view.showView(false);
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			Response values = new Response(false);
			for (Entry<?, ?> entry : map.entrySet()) {
				values.with(TypeUtils.castToString(entry.getKey()), entry.getValue());
			}
			return values;
		} else if (value instanceof Collection) {
			Collection<?> collection = (Collection<?>) value;
			Collection<Object> array = new ArrayList<Object>();
			for (Object item : collection) {
				array.add(this.parse(item));
			}
			return array;
		} else {
			Class<?> clazz = value.getClass();
			if (clazz.isEnum()) {
				return ((Enum<?>) value).name();
			} else if (clazz.isArray()) {
				int len = Array.getLength(value);
				Collection<Object> array = new ArrayList<Object>();
				for (int i = 0; i < len; ++i) {
					Object item = Array.get(value, i);
					array.add(this.parse(item));
				}
				return array;
			} else if (config.isPrimitive(clazz)) {
				return value;
			} else {
				FieldInfo[] properties = getFields(clazz);
				if (null != properties && properties.length > 0) {
					Response response = new Response(false);
					for (FieldInfo property : properties) {
						try {
							response.with(property.getName(), property.getMethod().invoke(value));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new BaseException(e);
						}
					}
					return response;
				}
				return null;
			}
		}

	}

	public static Response convert(Viewable bean) {
		return convert(bean, false);
	}

	private static Response convert(Viewable bean, boolean init) {
		Response response = new Response(init);
		if (null != bean) {
			FieldInfo[] properties = getFields(bean.getClass());
			if (null != properties && properties.length > 0) {
				for (FieldInfo property : properties) {
					try {
						response.with(property.getName(), property.getMethod().invoke(bean));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new BaseException(e);
					}
				}
			}
		}
		return response;
	}

	@Override
	public String toString() {
		return JSONObject.toJSON(this).toString();
	}

}
