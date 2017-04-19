package cn.hpc.util.lua;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// LuaState L = LuaStateFactory.newLuaState();
		// L.openLibs();
		// L.LdoString("text = 'Hello Android, I am Lua. 中文'");
		// L.getGlobal("text");
		// String text = L.toString(-1);
		// super.onCreate(savedInstanceState);
		// TextView tv = new TextView(this);
		// tv.setText(text);
		// setContentView(tv);

		TextView txtView = (TextView) this.findViewById(R.id.helloTxt);

		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();

		L.LdoString(loadAssetsString("lua/hello.lua"));

		// 实例1.Java调用lua函数
		L.getField(LuaState.LUA_GLOBALSINDEX, "plus"); // 取得lua里的plus函数
		L.pushNumber(12); // 传递参数1给plus函数
		L.pushNumber(2132); // 传递参数2给plus函数
		L.call(2, 1); // 调用plus函数

		L.setField(LuaState.LUA_GLOBALSINDEX, "a"); // 将函数的结果保存到一个参数a中
		LuaObject obj = L.getLuaObject("a"); // 取得参数a

		txtView.setText("" + obj.getString()); // 打印参数a的值到TextView中

		// lua调用Java对象
		CData value = new CData();
		L.getField(LuaState.LUA_GLOBALSINDEX, "heihei");// 获取(或者说，是定位？)lua的heihei函数
		try {
			L.pushObjectValue(value); // 将value对象传递给heihei函数
		} catch (LuaException e) {
		}
		L.call(1, 1); // 调用heihei函数
		L.setField(LuaState.LUA_GLOBALSINDEX, "v"); // 函数结果保存到参数v中
		LuaObject v = L.getLuaObject("v"); // 读取参数v
		try {
			txtView.setText("" + v.getObject()); // 打印日志
		} catch (LuaException e) {
		}

		L.close(); // 关闭lua
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// 嘿嘿，注意咯，精华来了~这个函数就是用来读取lua文件，//然后保存到一个字符串里（当然，你可以用其他方法，只要能保存成字符串）
	private String loadAssetsString(String resPath) {
		InputStream isread = null;
		byte[] luaByte = new byte[1];
		try { // 就是这里了，我们把lua 都放到asset目录下，这样系统就 //不会找不到文件路径了，哼~

			isread = this.getAssets().open(resPath);
			int len = isread.available();
			luaByte = new byte[len];
			isread.read(luaByte);
		} catch (IOException e1) {

		} finally {
			if (isread != null) {
				try {
					isread.close();
				} catch (IOException e) {
				}
			}
		}
		return EncodingUtils.getString(luaByte, "UTF-8");
	} 
	
	// 一个简单的内部类 
	class CData {

		private int num = 10;
	
		public void inc() {
			num++;
		}
	
		public String toString() {
			return "num is " + num;
		}
	}
}
