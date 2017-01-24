package windsekirun.example.appdiasbler;

import android.app.Activity;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

public class MainActivity extends Activity {
	
	public ListView list;
	public ArrayList<Menu> pd;
	public ListAdapter adapter;
	public ArrayList<String> applist, appname;
	public ArrayList<Drawable> appicon;
	public ApplicationInfo ai;
	public boolean isRoot;
	public PackageManager pm;
	public String line;
	public BufferedReader br;
	public ProgressDialog mDlg;
	public Scanner s;
	public static Handler unknown;
	public StringBuilder sb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState); 
	   setContentView(R.layout.activity_main) // just listview activity :)
	   pm = getActivity().getPackageManager();
	   list = (ListView) findViewById(R.id.list);
	   pd = new ArrayList<Menu>();
	   adapter = new ListAdapter(getActivity(), pd);
	   new LoadData().execute();
	   unknown = new Handler() {
			public void handleMessage(Message msg) {
				// Error at doesn't load application data
			}
		};
	}

	public class ClickData implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> a, View v, final int i, long l) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(appicon.get(i));
			builder.setTitle(appname.get(i));
			builder.setMessage("Disable " + appname.get(i) + "?");
			builder.setPositiveButton(android.R.string.yes, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					CommandCapture command = new CommandCapture(0, "pm disable " + applist.get(i));
					try {
						RootTools.getShell(true).add(command);
					} catch (Exception e) {}
					// Message to success disable application
				}
			});
			builder.setNegativeButton(android.R.string.no, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
		}
	}

	public class LoadData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(MainActivity.this);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setIndeterminate(false);
			mDlg.setCancelable(true);
			mDlg.setMessage("Loading...");
			mDlg.setCanceledOnTouchOutside(false);
			mDlg.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			mDlg.dismiss();
			list.setAdapter(adapter);
			list.setOnItemClickListener(new ClickData());
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			applist = new ArrayList<String>();
			appname = new ArrayList<String>();
			appicon = new ArrayList<Drawable>();
			sb = new StringBuilder();
			Command command = new Command(0, "pm list packages -s -e") {
				@Override
				public void output(int id, String line) {
					sb.append(line.substring(8));
					sb.append(System.getProperty("line.separator"));
				}
			};
			try {
				RootTools.getShell(true).add(command).waitForFinish();
			} catch (Exception e1) {}
			s = new Scanner(sb.toString());
			while (s.hasNextLine()) {
				applist.add(s.nextLine());
			}
			s.close();
			for (int i = 0; i < applist.size(); i++) {
				try {
					ai = pm.getApplicationInfo(applist.get(i), 0);
				} catch (Exception e) {}
				String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
				appname.add(applicationName);
				Drawable applicationIcon = (Drawable) (ai != null ? pm.getApplicationIcon(ai) : getResources().getDrawable(
						R.drawable.ic_launcher));
				appicon.add(applicationIcon);
				if (ai == null) {
					unknown.sendEmptyMessage(0);
				}
			}
			for (int i = 0; i < applist.size(); i++) {
				adapter.add(new Menu(appname.get(i), applist.get(i), appicon.get(i)));
			}
			return null;
		}
	}

	public class ListAdapter extends ArrayAdapter<Menu> {
		private LayoutInflater inflater;

		public ListAdapter(Context c, ArrayList<Menu> o) {
			super(c, 0, o);
			inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;
			if (v == null) {
				view = inflater.inflate(R.layout.row_disabler, null);
			} else {
				view = v;
			}
			final Menu data = this.getItem(position);
			TextView title = (TextView) view.findViewById(R.id.Title);
			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView subtext = (TextView) view.findViewById(R.id.subtext);
			title.setText(data.getTitle());
			subtext.setText(data.getSubText());
			icon.setImageDrawable(data.getIcon());
			return view;
		}
	}

	public class Menu {
		String Title, SubText;
		Drawable icon;

		public Menu(String t, String p, Drawable i) {
			Title = t;
			SubText = p;
			icon = i;
		}

		public String getTitle() {
			return Title;
		}

		public String getSubText() {
			return SubText;
		}

		public Drawable getIcon() {
			return icon;
		}
	}
}
