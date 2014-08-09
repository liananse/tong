/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * 将序列化的类 转换 byte
 * 
 * @author zenghui.wang
 * 
 */
public class SerializableInterface {

	public static byte[] serialize(Object o) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(o);
			out.close();
			mem_out.close();

			byte[] bytes = mem_out.toByteArray();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(mem_in);

			Object o = in.readObject();

			in.close();
			mem_in.close();
			return o;
		} catch (StreamCorruptedException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
