package in.sel.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;

import in.sel.indianbabyname.R;

public class DialogNameChange extends DialogFragment{

	public interface ActionListener
	{
		public void onSubmitListener();
		public void onCancelListener();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflate = getActivity().getLayoutInflater();
		builder.setView(inflate.inflate(R.layout.dialog_name_editor, null))
		.setPositiveButton("Submit", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDialog().cancel();
			}
		});
	    return builder.create();
	}
}
