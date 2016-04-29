package projekt.dashboard.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mutualmobile.cardstack.CardStackAdapter;
import com.tramsun.libs.prefcompat.Pref;

import projekt.dashboard.R;
import projekt.dashboard.colorpicker.ColorPickerDialog;

public class MyCardStackAdapter extends CardStackAdapter implements
        CompoundButton.OnCheckedChangeListener {
    private static int[] bgColorIds;
    private final LayoutInflater mInflater;
    private final Context mContext;
    public Runnable updateSettingsView;
    public SharedPreferences prefs;
    public boolean colorful_icon = true;

    // ==================================== Settings Tweaks ================================== //
    public boolean category_title_caps = true;
    public boolean category_title_bold = true;
    public boolean category_title_italics = true;
    public boolean dashboard_dividers = true;
    public boolean dirtytweaks_iconpresence = true;
    public boolean dashboard_rounding = false;
    public int current_selected_settings_icon_color = Color.argb(255, 255, 255, 255);
    public int current_selected_settings_title_color = Color.argb(255, 255, 255, 255);
    public int current_selected_qs_accent_color = Color.argb(255, 255, 255, 255);

    // ==================================== SystemUI Tweaks ================================== //
    public int current_selected_qs_tile_color = Color.argb(255, 255, 255, 255);
    public int current_selected_qs_text_color = Color.argb(255, 255, 255, 255);
    public int current_selected_system_accent_color = Color.argb(255, 255, 255, 255); // White

    // ==================================== Framework Tweaks ================================ //
    public int current_selected_system_accent_dark_color = Color.argb(255, 255, 255, 255); // White
    public int current_selected_system_accent_dual_color = Color.argb(255, 119, 119, 119); // Medium Grey
    public int current_selected_system_accent_light_color = Color.argb(255, 119, 119, 119); // Medium grey
    public int current_selected_system_appbg_color = Color.argb(255, 0, 0, 0); // Black
    public int current_selected_system_appbg_light_color = Color.argb(255, 215, 215, 215); // Light grey
    public int current_selected_system_dialog_color = Color.argb(191, 0, 0, 0); // Black with Transparency
    public int current_selected_system_dialog_light_color = Color.argb(191, 238, 238, 238); // Light Grey with Transparency
    public int current_selected_system_notifications_primary_color = Color.argb(255, 255, 255, 255); // White
    public int current_selected_system_notifications_secondary_color = Color.argb(255, 174, 174, 174); // Lighter grey
    public int current_selected_system_ripple_color = Color.argb(74, 119, 119, 119); // Medium Grey with Transparency
    public int current_selected_system_main_color = Color.argb(255, 33, 32, 33); // Main theme color

    private Logger log = new Logger(MyCardStackAdapter.class.getSimpleName());


    public MyCardStackAdapter(Activity activity) {
        super(activity);
        mContext = activity;
        mInflater = LayoutInflater.from(activity);
        bgColorIds = new int[]{
                R.color.card_bg, // Theme Selection
                R.color.card1_bg, // Settings
                R.color.card2_bg, // SystemUI
                R.color.card3_bg, // Framework
                R.color.card4_bg, // Commons Modifications
        };
    }

    @Override
    public int getCount() {
        return bgColorIds.length;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        log.d("onCheckedChanged() called with: " + "buttonView = [" + buttonView + "], " +
                "isChecked = [" + isChecked + "]");
        Pref.putBoolean(CardStackPrefs.PARALLAX_ENABLED, isChecked);
        Pref.putBoolean(CardStackPrefs.SHOW_INIT_ANIMATION, isChecked);
        updateSettingsView.run();
    }

    @Override
    public View createView(int position, ViewGroup container) {
        if (position == 0) return getThemeSelectorView(container);
        if (position == 1) return getSettingsView(container);
        if (position == 2) return getSystemUIView(container);
        if (position == 3) return getFrameworksView(container);
        if (position == 4) return getCommonsView(container);

        CardView root = (CardView) mInflater.inflate(R.layout.card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[position]));
        TextView cardTitle = (TextView) root.findViewById(R.id.card_title);
        cardTitle.setText(mContext.getResources().getString(R.string.card_title, position));
        return root;
    }

    private View getThemeSelectorView(ViewGroup container) {
        CardView root = (CardView) mInflater.inflate(
                R.layout.theme_selection_card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[0]));

        return root;
    }


    private View getSettingsView(ViewGroup container) {
        CardView root = (CardView) mInflater.inflate(R.layout.settings_card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[1]));

        final ImageView wifiIcon = (ImageView) root.findViewById(R.id.wifiIcon);
        final TextView categoryHeader = (TextView) root.findViewById(R.id.categoryHeaderTitle);


        // Colorful DU/PN Tweaks Icon

        final Switch colorful_icon_switch = (Switch) root.findViewById(R.id.colorful_icon);
        colorful_icon_switch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            colorful_icon = true;
                            Log.e("Switch Colorful Icon", colorful_icon + "");
                        } else {
                            colorful_icon = false;
                            Log.e("Switch Colorful Icon", colorful_icon + "");
                        }
                    }
                });


        // Dashboard Categories (Rounded)

        final Switch dashboard_round = (Switch) root.findViewById(R.id.dashboard_rounding);
        dashboard_round.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            dashboard_rounding = true;
                            Log.e("Dashboard (Rounded)", dashboard_rounding + "");
                        } else {
                            dashboard_rounding = false;
                            Log.e("Dashboard (Rounded)", dashboard_rounding + "");
                        }
                    }
                });

        // Dashboard Categories Title (All Caps)

        final Switch categories_title_caps = (Switch) root.findViewById(
                R.id.dashboard_title_allcaps);
        categories_title_caps.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            category_title_caps = true;
                            Log.e("Categories Title (Caps)", category_title_caps + "");
                        } else {
                            category_title_caps = false;
                            Log.e("Categories Title (Caps)", category_title_caps + "");
                        }
                    }
                });

        // Dashboard Categories Title (Bold)

        final Switch categories_title_bold = (Switch) root.findViewById(
                R.id.dashboard_title_bold);
        categories_title_bold.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            category_title_bold = true;
                            Log.e("Categories Title (Bold)", category_title_bold + "");
                        } else {
                            category_title_bold = false;
                            Log.e("Categories Title (Bold)", category_title_bold + "");
                        }
                    }
                });

        // Dashboard Categories Title (Italics)

        final Switch categories_title_italics = (Switch) root.findViewById(
                R.id.dashboard_title_italics);
        categories_title_italics.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            category_title_italics = true;
                            Log.e("Categories Title (Ita)", category_title_italics + "");
                        } else {
                            category_title_italics = false;
                            Log.e("Categories Title (Ita)", category_title_italics + "");
                        }
                    }
                });

        // Dashboard Dividers

        final Switch dashboard_divider = (Switch) root.findViewById(R.id.dashboard_dividers);
        dashboard_divider.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            dashboard_dividers = true;
                            Log.e("Dashboard Dividers", dashboard_dividers + "");
                        } else {
                            dashboard_dividers = false;
                            Log.e("Dashboard Dividers", dashboard_dividers + "");
                        }
                    }
                });

        // Dirty Tweaks Icon Presence

        final Switch dutweaks_icons = (Switch) root.findViewById(R.id.dirty_tweaks_icons);
        dutweaks_icons.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            dirtytweaks_iconpresence = true;
                            Log.e("DU Tweaks Icon", dirtytweaks_iconpresence + "");
                        } else {
                            dirtytweaks_iconpresence = false;
                            Log.e("DU Tweaks Icon", dirtytweaks_iconpresence + "");
                        }
                    }
                });

        // Settings Icons Colors

        final ImageView settings_icon_colors = (ImageView) root.findViewById(
                R.id.settings_icon_colorpicker);
        settings_icon_colors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_settings_icon_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_settings_icon_color = color;
                        settings_icon_colors.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        wifiIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Settings Title Colors

        final ImageView settings_title_colors = (ImageView) root.findViewById(
                R.id.settings_title_colorpicker);
        settings_title_colors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_settings_title_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_settings_title_color = color;
                        settings_title_colors.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        categoryHeader.setTextColor(color);
                    }
                });
                cpd.show();
            }
        });

        return root;
    }

    private View getSystemUIView(ViewGroup container) {
        CardView root = (CardView) mInflater.inflate(R.layout.systemui_card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[2]));

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        final TextView wifiLabel = (TextView) root.findViewById(R.id.wifiLabel);
        final TextView bluetoothLabel = (TextView) root.findViewById(R.id.bluetoothLabel);
        wifiLabel.setText(prefs.getString("dashboard_username",
                root.getResources().getString(R.string.systemui_preview_default_no_username)) +
                root.getResources().getString(R.string.systemui_preview_label));
        final SeekBar brightness = (SeekBar) root.findViewById(R.id.seekBar);

        // QS Accent Colors

        final ImageView qs_accents = (ImageView) root.findViewById(R.id.qs_accent_colorpicker);
        qs_accents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_qs_accent_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_qs_accent_color = color;
                        qs_accents.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        ColorStateList csl = new ColorStateList(
                                new int[][]{
                                        new int[]{android.R.attr.state_pressed},
                                        new int[]{android.R.attr.state_focused},
                                        new int[]{}
                                },
                                new int[]{
                                        color, color, color
                                }
                        );
                        brightness.setProgressTintList(csl);
                        brightness.setThumbTintList(csl);
                    }
                });
                cpd.show();
            }
        });

        // QS Icon Colors

        final ImageView qs_tile = (ImageView) root.findViewById(R.id.qs_tile_icon_colorpicker);
        qs_tile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_qs_tile_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_qs_tile_color = color;
                        qs_tile.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        ColorStateList csl = new ColorStateList(
                                new int[][]{
                                        new int[]{android.R.attr.state_pressed},
                                        new int[]{android.R.attr.state_focused},
                                        new int[]{}
                                },
                                new int[]{
                                        color, color, color
                                }
                        );
                        bluetoothLabel.setCompoundDrawableTintList(csl);
                        wifiLabel.setCompoundDrawableTintList(csl);
                    }
                });
                cpd.show();
            }
        });

        // QS Title Colors

        final ImageView qs_text = (ImageView) root.findViewById(R.id.qs_tile_text_colorpicker);
        qs_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_qs_text_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_qs_text_color = color;
                        qs_text.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        wifiLabel.setTextColor(color);
                        bluetoothLabel.setTextColor(color);
                    }
                });
                cpd.show();
            }
        });

        return root;
    }

    private View getFrameworksView(ViewGroup container) {
        CardView root = (CardView) mInflater.inflate(R.layout.framework_card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[3]));

        final android.support.v7.widget.Toolbar framework_toolbar =
                (android.support.v7.widget.Toolbar) root.findViewById(R.id.framework_toolbar);
        framework_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        final Switch switch1 = (Switch) root.findViewById(R.id.switch_example);
        final Switch switch2 = (Switch) root.findViewById(R.id.switch_example2);

        final RelativeLayout rl = (RelativeLayout) root.findViewById(R.id.main_relativeLayout);

        // Framework Accent (universal)

        final ImageView accent_universal = (ImageView) root.findViewById(
                R.id.system_accent_colorpicker);
        accent_universal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_accent_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_accent_color = color;
                        accent_universal.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        ColorStateList csl = new ColorStateList(
                                new int[][]{
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                                new int[]{
                                        color, color
                                }
                        );
                        switch1.setTrackTintList(csl);
                        switch1.setThumbTintList(csl);
                    }
                });
                cpd.show();
            }
        });

        // Framework Accent (dual)

        final ImageView accent_secondary = (ImageView) root.findViewById(
                R.id.system_accent_dual_colorpicker);
        accent_secondary.setColorFilter(
                current_selected_system_accent_dual_color, PorterDuff.Mode.SRC_ATOP);
        accent_secondary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_accent_dual_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_accent_dual_color = color;
                        accent_secondary.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Framework Accent (light)

        final ImageView accent_light = (ImageView) root.findViewById(
                R.id.system_accent_light_colorpicker);
        accent_light.setColorFilter(
                current_selected_system_accent_light_color, PorterDuff.Mode.SRC_ATOP);
        accent_light.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_accent_light_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_accent_light_color = color;
                        accent_light.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        ColorStateList csl = new ColorStateList(
                                new int[][]{
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                                new int[]{
                                        color, color
                                }
                        );
                        switch2.setTrackTintList(csl);
                        switch2.setThumbTintList(csl);
                    }
                });
                cpd.show();
            }
        });

        // Framework Appbg (dark)

        final ImageView appbg_dark = (ImageView) root.findViewById(
                R.id.system_appbg_colorpicker);
        rl.setBackgroundColor(current_selected_system_appbg_color);
        appbg_dark.setColorFilter(current_selected_system_appbg_color, PorterDuff.Mode.SRC_ATOP);
        appbg_dark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_appbg_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_appbg_color = color;
                        appbg_dark.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        rl.setBackgroundColor(color);
                    }
                });
                cpd.show();
            }
        });

        // Framework Appbg (light)

        final ImageView appbg_light = (ImageView) root.findViewById(
                R.id.system_appbg_light_colorpicker);
        appbg_light.setColorFilter(
                current_selected_system_appbg_light_color, PorterDuff.Mode.SRC_ATOP);
        switch2.setBackgroundColor(current_selected_system_appbg_light_color);
        appbg_light.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_appbg_light_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_appbg_light_color = color;
                        appbg_light.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        switch2.setBackgroundColor(color);
                    }
                });
                cpd.show();
            }
        });

        // Framework System Dialog Color (dark)

        final ImageView dialog_dark = (ImageView) root.findViewById(
                R.id.system_dialog_colorpicker);
        dialog_dark.setColorFilter(current_selected_system_dialog_color, PorterDuff.Mode.SRC_ATOP);
        dialog_dark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_dialog_color);
                cpd.setAlphaSliderVisible(true);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_dialog_color = color;
                        dialog_dark.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Framework System Dialog Color (light)

        final ImageView dialog_light = (ImageView) root.findViewById(
                R.id.system_dialog_light_colorpicker);
        dialog_light.setColorFilter(
                current_selected_system_dialog_light_color, PorterDuff.Mode.SRC_ATOP);
        dialog_light.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_dialog_light_color);
                cpd.setAlphaSliderVisible(true);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_dialog_light_color = color;
                        dialog_light.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Framework System Main Color

        final ImageView main_color = (ImageView) root.findViewById(
                R.id.system_main_colorpicker);
        main_color.setColorFilter(current_selected_system_main_color, PorterDuff.Mode.SRC_ATOP);
        main_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_main_color);
                cpd.setAlphaSliderVisible(true);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_main_color = color;
                        main_color.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        framework_toolbar.setBackgroundColor(color);
                    }
                });
                cpd.show();
            }
        });

        // Framework Notifications Primary Color

        final ImageView notifications_primary = (ImageView) root.findViewById(
                R.id.system_notification_text_1_colorpicker);
        notifications_primary.setColorFilter(
                current_selected_system_notifications_primary_color, PorterDuff.Mode.SRC_ATOP);
        notifications_primary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_notifications_primary_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_notifications_primary_color = color;
                        notifications_primary.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Framework Notifications Secondary Color

        final ImageView notifications_secondary = (ImageView) root.findViewById(
                R.id.system_notification_text_2_colorpicker);
        notifications_secondary.setColorFilter(
                current_selected_system_notifications_secondary_color, PorterDuff.Mode.SRC_ATOP);
        notifications_secondary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_notifications_secondary_color);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_notifications_secondary_color = color;
                        notifications_secondary.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });

        // Framework Ripple Color

        final ImageView ripples = (ImageView) root.findViewById(
                R.id.system_ripple_colorpicker);
        ripples.setColorFilter(current_selected_system_ripple_color, PorterDuff.Mode.SRC_ATOP);
        ripples.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ColorPickerDialog cpd = new ColorPickerDialog(
                        mContext, current_selected_system_ripple_color);
                cpd.setAlphaSliderVisible(true);
                cpd.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        current_selected_system_ripple_color = color;
                        ripples.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                cpd.show();
            }
        });


        return root;
    }

    private View getCommonsView(ViewGroup container) {
        CardView root = (CardView) mInflater.inflate(R.layout.commons_card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[4]));

        return root;
    }
}