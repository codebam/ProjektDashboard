package projekt.dashboard.layers.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.isseiaoki.simplecropview.CropImageView;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import projekt.dashboard.layers.R;
import projekt.dashboard.layers.fragments.base.BasePageFragment;
import projekt.dashboard.layers.util.LayersFunc;

/**
 * @author Adityata
 */
public class HeaderSwapperFragment extends BasePageFragment {

    private static int RESULT_LOAD_IMAGE = 1;
    public ViewGroup inflation;
    public boolean is_all_selected, is_picture_selected, are_we_clearing_Files_after,
            free_crop_mode, swap_contextual_header, is_radio_selected;
    public CropImageView cropImageView;
    public ImageView croppedImageView;
    public Bitmap croppedBitmap;
    public Spinner spinner, spinner1;
    public String theme_dir, package_name;
    public FloatingActionButton apply_fab;
    public Button saveButton;
    public int folder_directory = 1;
    public int current_hour;
    public TextView checkBoxInstructions, currentTimeVariable;
    public CheckBox freeCropMode, swapcontext;
    public SharedPreferences prefs;
    public String vendor = "/system/vendor/overlay";
    public String mount = "/system";
    public boolean xhdpi = false;
    public boolean xxhdpi = true;
    public boolean xxxhdpi = false;
    boolean log = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflation = (ViewGroup) inflater.inflate(
                R.layout.fragment_headerswapper, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (prefs.getBoolean("dialog", true)) {
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle("Header Swapper :)");
            ad.setMessage("Woah Welcome to Header Swapper,a place where you can actually use your" +
                    " favourite moments,your memories, right next to your notifications.\nSo How " +
                    "to use it:-\n1. Lets Click on the Click on the Floating button\n2. And yes " +
                    "if your ROM supports contextual headers you can always click on swap " +
                    "contextual headers and swap them too.\n3. OK, So Now After clicking the " +
                    "Floating Button just choose and crop the image.\n4. Now click on Save Button" +
                    " and Let the Magic Begin.\n\nIMP :- The Phone Should Automatically " +
                    "Softreboot to make changes");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (new LayersFunc(getActivity()).isAppInstalled(getActivity(), "com.chummy" +
                            ".aditya.materialdark.layers.donate")) {
                        startActivity(new Intent().setComponent(new ComponentName("com.lovejoy777" +
                                ".rroandlayersmanager", "com.lovejoy777.rroandlayersmanager" +
                                ".MainActivity")));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play" +
                                ".google.com/store/apps/details?id=com.chummy.aditya.materialdark" +
                                ".layers.donate")));
                    }
                }
            });
            ad.setNeutralButton("Dont Show again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putBoolean("dialog", false).commit();
                }
            });
        }
        apply_fab = (FloatingActionButton) inflation.findViewById(R.id.apply_fab);
        if (prefs.getBoolean("blacked_out_enabled", true)) {
            apply_fab.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.primary_1_blacked_out)));
        } else {
            apply_fab.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.primary_1_dark_material)));
        }
        apply_fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(getImageSelectionIntent(), RESULT_LOAD_IMAGE);
            }
        });

        freeCropMode = (CheckBox) inflation.findViewById(R.id.checkBox2);
        freeCropMode.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            free_crop_mode = true;
                            Log.d("CheckBox",
                                    "Free crop mode for Image Cropper has been ENABLED.");
                        } else {
                            free_crop_mode = false;
                            Log.d("CheckBox",
                                    "Free crop mode for Image Cropper has been DISABLED.");
                        }
                    }
                });

        final LinearLayout ln = (LinearLayout) inflation.findViewById(R.id.dpi);
        ln.setVisibility(View.GONE);

        swapcontext = (CheckBox) inflation.findViewById(R.id.checkBox3);
        swapcontext.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            swap_contextual_header = true;
                            ln.setVisibility(View.VISIBLE);
                            Log.d("CheckBox", "Universal variable to advanced log ENABLED.");
                        } else {
                            swap_contextual_header = false;
                            ln.setVisibility(View.GONE);
                            Log.d("CheckBox", "Universal variable to advanced log DISABLED.");
                        }
                    }
                });
        RadioGroup radioGroup = (RadioGroup) inflation.findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio:
                        xhdpi = true;
                        xxhdpi = false;
                        xxxhdpi = false;
                        is_radio_selected = true;
                        break;
                    case R.id.radio2:
                        xhdpi = false;
                        xxhdpi = true;
                        xxxhdpi = false;
                        is_radio_selected = true;
                        break;
                    case R.id.radio3:
                        xhdpi = false;
                        xxhdpi = false;
                        xxxhdpi = true;
                        is_radio_selected = true;
                        break;
                }
                if (swap_contextual_header && is_radio_selected) {
                    apply_fab.show();
                } else {
                    apply_fab.hide();
                }
            }
        });
        checkBoxInstructions = (TextView) inflation.findViewById(R.id.textView2);
        saveButton = (Button) inflation.findViewById(R.id.save_button);

        return inflation;
    }

    public void changeFABaction() {
        if (!is_picture_selected) {
            apply_fab.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_photo_library_24dp));
            if (prefs.getBoolean("blacked_out_enabled", true)) {
                apply_fab.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.primary_1_blacked_out)));
            } else {
                apply_fab.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.primary_1_dark_material)));
            }
            apply_fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivityForResult(getImageSelectionIntent(), RESULT_LOAD_IMAGE);
                    is_picture_selected = true;
                }
            });
        } else {
            apply_fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_cached_24dp));
            apply_fab.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.resetButton)));
            apply_fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    resetImageViews();
                }
            });
        }
    }

    private Intent getImageSelectionIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        return intent;
    }

    public void letsGetStarted() {
        String[] secondPhaseCommands = {LayersFunc.vendor + "/" + LayersFunc.themeframework + "" +
                ".apk"};
        new copyThemeFiles().execute(secondPhaseCommands);
        Log.d("letsGetStarted", secondPhaseCommands[0]);

        new secondPhaseAsyncTasks().execute(secondPhaseCommands);

        Button softReboot = (Button) inflation.findViewById(R.id.softreboot);
        softReboot.setVisibility(View.VISIBLE);
        softReboot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                eu.chainfire.libsuperuser.Shell.SU.run("killall zygote");
            }
        });
    }

    public void resetImageViews() {
        ImageView image_to_crop = (ImageView) inflation.findViewById(R.id.cropImageView);
        image_to_crop.setVisibility(View.GONE);
        Button cropButton = (Button) inflation.findViewById(R.id.crop_button);
        cropButton.setVisibility(View.GONE);
        ImageView croppedImage = (ImageView) inflation.findViewById(R.id.croppedImageView);
        croppedImage.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        checkBoxInstructions.setVisibility(View.VISIBLE);
        freeCropMode.setVisibility(View.VISIBLE);
        swapcontext.setVisibility(View.VISIBLE);
        is_picture_selected = false;
        changeFABaction();
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE &&
                resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            is_picture_selected = true;
            changeFABaction();
            final ImageView image_to_crop = (ImageView) inflation.findViewById(R.id.cropImageView);
            image_to_crop.setVisibility(View.VISIBLE);
            cropImageView = (CropImageView) inflation.findViewById(R.id.cropImageView);
            checkBoxInstructions.setVisibility(View.GONE);
            freeCropMode.setVisibility(View.GONE);
            swapcontext.setVisibility(View.GONE);
            if (!free_crop_mode) {
                cropImageView.setCustomRatio(4, 1);
            }
            croppedImageView = (ImageView) inflation.findViewById(R.id.croppedImageView);
            cropImageView.setImageBitmap(bitmap);
            //https://github.com/IsseiAoki/SimpleCropView/issues/45
            //cropImageView.setImageURI(selectedImage);
            final Button cropButton = (Button) inflation.findViewById(R.id.crop_button);
            cropButton.setVisibility(View.VISIBLE);
            cropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView croppedImage = (ImageView) inflation.findViewById(
                            R.id.croppedImageView);
                    croppedImage.setVisibility(View.VISIBLE);
                    croppedBitmap = cropImageView.getCroppedBitmap();
                    croppedImageView.setImageBitmap(cropImageView.getCroppedBitmap());
                    saveButton.setVisibility(View.VISIBLE);
                    cropButton.setVisibility(View.GONE);
                    image_to_crop.setVisibility(View.GONE);
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View V) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    croppedBitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
                    File directory = new File(getActivity().getFilesDir(),
                            "/res/drawable/");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File selected = new File(getActivity().getFilesDir() +
                            "/res/drawable/",
                            "menuitem_background.png");
                    if (swap_contextual_header) {
                        String[] customheader = getResources().getStringArray(R.array
                                .contextual_headers);
                        for (int loop = 0; loop < 10; loop++) {
                            File file = new File(getActivity().getFilesDir() +
                                    "/res/drawable-xxhdpi-v4/",
                                    customheader[loop]);
                            try {
                                file.createNewFile();
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(bytes.toByteArray());
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.getStackTrace();
                            }
                        }
                    }
                    try {
                        selected.createNewFile();
                        FileOutputStream so = new FileOutputStream(selected);
                        so.write(bytes.toByteArray());
                        so.close();

                    } catch (IOException e) {
                        Log.d("ImageSaver",
                                "Unable to save new file");
                    }
                    resetImageViews();
                    letsGetStarted();

                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public int getTitle() {
        return R.string.contextualheaderswapper;
    }

    public class copyThemeFiles extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String sourcePath = params[0];
            File source = new File(sourcePath);
            String destinationPath = getActivity().getFilesDir().getAbsolutePath() +
                    "/" + LayersFunc.themeframework + ".apk";
            File destination = new File(destinationPath);

            String sourcePathsys = vendor + "/" + LayersFunc.themesystemui;
            File sourcesys = new File(sourcePathsys);
            String destinationPathsys = getActivity().getFilesDir().getAbsolutePath() +
                    "/" + LayersFunc.themesystemui + ".apk";
            File destinationsys = new File(destinationPathsys);

            try {
                FileUtils.copyFile(source, destination);
                if (swap_contextual_header) {
                    FileUtils.copyFile(sourcesys, destinationsys);
                }
                Log.d("Progress", "1");
                Log.d("CopyFrameworkFile",
                        "Successfully copied framework apk from overlays folder to work directory");
            } catch (IOException e) {
                Log.d("CopyFrameworkFile",
                        "Failed to copy framework apk from resource-Files to work directory");
                e.printStackTrace();
            }
            return null;
        }

    }

    private class secondPhaseAsyncTasks extends AsyncTask<String, String, Void> {

        private ProgressDialog pd;

        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.d("Progress", "2");
                performAAPTonCommonsAPK();
            } catch (Exception e) {
                Log.d("performAAPTonCommonsAPK",
                        "Could not process file.");
            }
            return null;
        }

        protected void onPreExecute() {
            String[] responses = getResources().getStringArray(R.array.dialog_responses);

            int idx = new Random().nextInt(responses.length);
            String random = (responses[idx]);

            pd = new ProgressDialog(getActivity());
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage(random);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        protected void onPostExecute(Void result) {
            pd.dismiss();
            eu.chainfire.libsuperuser.Shell.SU.run("busybox killall com.android.systemui");

        }

        public List processor() {
            List<String> filenamePNGs = Arrays.asList(
                    "notifhead_afternoon.png", "notifhead_christmas.png", "notifhead_morning.png",
                    "notifhead_newyearseve.png", "notifhead_night.png", "notifhead_noon.png",
                    "notifhead_sunrise.png", "notifhead_sunset_hdpi.png",
                    "notifhead_sunset_xhdpi.png", "notifhead_sunset.png");

            List<String> list = new ArrayList<String>();

            File f2 = new File(
                    getActivity().getFilesDir().getAbsolutePath() + "/res/drawable-xxhdpi-v4/");
            File[] files2 = f2.listFiles();
            if (files2 != null) {
                for (File inFile2 : files2) {
                    if (inFile2.isFile()) {
                        // Filter out filenames of which were unzipped earlier
                        String filenameParse[] = inFile2.getAbsolutePath().split("/");
                        String filename = filenameParse[filenameParse.length - 1];

                        if (filenamePNGs.contains(filename)) {
                            list.add(filename);
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                }
                return list;
            }
            return null;
        }
        
        private void performAAPTonCommonsAPK() {
            Log.d("performAAPTonCommonsAPK",
                    "Mounting system as read-write as we prepare for some commands...");
            try {
                eu.chainfire.libsuperuser.Shell.SU.run("mount -o remount,rw /");
                eu.chainfire.libsuperuser.Shell.SU.run("mkdir /res");
                eu.chainfire.libsuperuser.Shell.SU.run("mkdir /res/drawable");
                if (xhdpi) {
                    eu.chainfire.libsuperuser.Shell.SU.run("mkdir /res/drawable-xhdpi-v4");
                }
                eu.chainfire.libsuperuser.Shell.SU.run("mkdir /res/drawable-xxhdpi-v4");
                if (xxxhdpi) {
                    eu.chainfire.libsuperuser.Shell.SU.run("mkdir /res/drawable-xxxhdpi-v4");
                }
                Log.d("Progress", "3");
                Log.d("Made Directory", "Made");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                "/res/drawable/menuitem_background.png " +
                                "/res/drawable/menuitem_background.png");
                Log.d("drawable", "dr");


                eu.chainfire.libsuperuser.Shell.SU.run(
                        "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                "/res/drawable/menuitem_background.png " +
                                "/res/drawable-xhdpi-v4/menuitem_background.png");
                Log.d("drawable", "x");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                "/res/drawable/menuitem_background.png " +
                                "/res/drawable-xxhdpi-v4/menuitem_background.png");
                Log.d("drawable", "xx");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                "/res/drawable/menuitem_background.png " +
                                "/res/drawable-xxxhdpi-v4/menuitem_background.png");
                Log.d("drawable", "xxx");
                Log.d("Progress", "4");
                if (swap_contextual_header) {
                    List source = processor();
                    for (int i = 0; i < source.size(); i++) {
                        eu.chainfire.libsuperuser.Shell.SU.run(
                                "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                        "/res/drawable-xxhdpi-v4/" + source.get(i) +
                                        " /res/drawable-xxhdpi-v4/" +
                                        source.get(i));
                    }
                    if (xhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            eu.chainfire.libsuperuser.Shell.SU.run(
                                    "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/res/drawable-xhdpi-v4/" + source.get(i) +
                                            " /res/drawable-xhdpi-v4/" +
                                            source.get(i));
                        }
                    }
                    if (xxxhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            eu.chainfire.libsuperuser.Shell.SU.run(
                                    "cp " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/res/drawable-xxxhdpi-v4/" + source.get(i) +
                                            " /res/drawable-xxxhdpi-v4/" +
                                            source.get(i));
                        }
                    }
                    Log.d("performAAPTonCommonsAPK",
                            "Successfully copied all drawables into the root folder.");
                }

                Log.d("Progress", "5");
                Log.d("performAAPTonCommonsAPK",
                        "Preparing for clean up on resources...");
                Process nativeApp3 = Runtime.getRuntime().exec(
                        "aapt remove " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable/menuitem_background.png");
                nativeApp3.waitFor();
                Process nativeAppx = Runtime.getRuntime().exec(
                        "aapt remove " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xhdpi-v4/menuitem_background.png");
                nativeAppx.waitFor();
                Process nativeAppxx = Runtime.getRuntime().exec(
                        "aapt remove " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xxhdpi-v4/menuitem_background.png");
                nativeAppxx.waitFor();
                Process nativeAppxxx = Runtime.getRuntime().exec(
                        "aapt remove " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xxxhdpi-v4/menuitem_background.png");
                Log.d("performAAPTonCommonsAPK",
                        "Deleted main drawable file!");
                nativeAppxxx.waitFor();
                Log.d("Progress", "6");
                if (swap_contextual_header) {
                    List source = processor();
                    for (int i = 0; i < source.size(); i++) {
                        Process nativeApp1 = Runtime.getRuntime().exec(
                                "aapt remove " + getActivity().getFilesDir().getAbsolutePath() +
                                        "/" + LayersFunc.themesystemui + ".apk " +
                                        "res/drawable-xxhdpi-v4" +
                                        source.get(i));
                        nativeApp1.waitFor();
                    }
                    if (xhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            Process nativeApp1 = Runtime.getRuntime().exec(
                                    "aapt remove " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/" + LayersFunc.themesystemui + ".apk " +
                                            "res/drawable-xhdpi-v4" +
                                            source.get(i));
                            nativeApp1.waitFor();
                        }
                    }
                    if (xxxhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            Process nativeApp1 = Runtime.getRuntime().exec(
                                    "aapt remove " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/" + LayersFunc.themesystemui + ".apk " +
                                            "res/drawable-xxxhdpi-v4" +
                                            source.get(i));
                            nativeApp1.waitFor();
                        }
                    }
                    Log.d("performAAPTonCommonsAPK",
                            "Deleted all drawable files!");
                }
                Log.d("Progress", "7");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "aapt add " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable/menuitem_background.png");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "aapt add " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xhdpi-v4/menuitem_background.png");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "aapt add " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xxhdpi-v4/menuitem_background.png");
                eu.chainfire.libsuperuser.Shell.SU.run(
                        "aapt add " +
                                getActivity().getFilesDir().getAbsolutePath() +
                                "/" + LayersFunc.themeframework + ".apk " +
                                "res/drawable-xxxhdpi-v4/menuitem_background.png");
                if (swap_contextual_header) {
                    List source = processor();
                    for (int i = 0; i < source.size(); i++) {
                        eu.chainfire.libsuperuser.Shell.SU.run(
                                "aapt add " + getActivity().getFilesDir().getAbsolutePath() +
                                        "/" + LayersFunc.themesystemui + ".apk " +
                                        "res/drawable-xxhdpi-v4" +
                                        source.get(i));
                    }
                    if (xhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            eu.chainfire.libsuperuser.Shell.SU.run(
                                    "aapt add " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/" + LayersFunc.themesystemui + ".apk " +
                                            "res/drawable-xhdpi-v4" +
                                            source.get(i));
                        }
                    }
                    if (xxxhdpi) {
                        for (int i = 0; i < source.size(); i++) {
                            eu.chainfire.libsuperuser.Shell.SU.run(
                                    "aapt add " + getActivity().getFilesDir().getAbsolutePath() +
                                            "/" + LayersFunc.themesystemui + ".apk " +
                                            "res/drawable-xxxhdpi-v4" +
                                            source.get(i));
                        }
                    }
                    Log.d("performAAPTonCommonsAPK",
                            "Added freshly created photo files...ALL DONE!");
                }
                Log.d("Progress", "8");
                eu.chainfire.libsuperuser.Shell.SU.run("rm -r /res/drawable");
                eu.chainfire.libsuperuser.Shell.SU.run("mount -o remount,ro /");
                Log.d("performAAPTonCommonsAPK",
                        "Cleaned up root directory and remounted system as read-only.");
                if (LayersFunc.checkBitPhone()) {
                    LayersFunc.copyFABFinalizedAPK(getActivity(), LayersFunc.themeframework, true);
                    Log.d("Progress", "9");
                    if (swap_contextual_header) {
                        LayersFunc.copyFABFinalizedAPK(getActivity(), LayersFunc.themesystemui,
                                true);
                        Log.d("Progress", "9");
                    }
                } else {
                    LayersFunc.copyFinalizedAPK(getActivity(), LayersFunc.themeframework, true);
                    Log.d("Progress", "9");
                    if (swap_contextual_header) {
                        LayersFunc.copyFinalizedAPK(getActivity(), LayersFunc.themesystemui, true);
                        Log.d("Progress", "9");
                    }
                }

                Log.d("Progress", "10");
                eu.chainfire.libsuperuser.Shell.SU.run("killall zygote");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
