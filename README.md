# nCovid-19
CoronaVirus Tracker Android App

<img src="https://raw.githubusercontent.com/hmtri-it/nCovid-19/master/screenshots/Screenshot_1584513041.png" width="200"> <img src="https://raw.githubusercontent.com/hmtri-it/nCovid-19/master/screenshots/Screenshot_1584513056.png" width="200"> <img src="https://raw.githubusercontent.com/hmtri-it/nCovid-19/master/screenshots/Screenshot_1584513070.png" width="200">

## Feature (Update ...)

- News Feed
- Country wise searching
- PieChart

# API used 

https://corona.lmao.ninja/ 

##  Tech Specification
- AndroidX
- Java 8
- Retrofit for network request
- ViewModel
- Gson
- Recyclerview
- UI Material
- MPAndroidChart

## Project structure

* screen
	* fragment
	* activity
	* dialog
	* widget
	* adapter
* service
* data
	* model
* util

## 1. Naming files

### 1.1 Class files

Class name must be written in [UpperCamelCase](https://en.wikipedia.org/wiki/CamelCase)

For class that extended in Android, class name should end with the name of the component

For example: MainActivity, HomeFragment,....

### 1.2 Resources files <Resources>

Resources file names are written in lowercase_underscore

#### 1.2.1 Drawable files

Rule for naming Drawables:


| Type   | Prefix            |		Example               |
|--------------| ------------------|-----------------------------|
| Action bar   | `ab_`             | `ab_stacked.9.png`          |
| Button       | `btn_`	            | `btn_send_pressed.9.png`    |
| Dialog       | `dialog_`         | `dialog_top.9.png`          |
| Divider      | `divider_`        | `divider_horizontal.9.png`  |
| Icon         | `ic_`	            | `ic_star.png`               |
| Menu         | `menu_	`           | `menu_submenu_bg.9.png`     |
| Notification | `notification_`	| `notification_bg.9.png`     |
| Tabs         | `tab_`            | `tab_pressed.9.png`         |

Rule for naming Icon (Theo [Android iconography guidelines](http://developer.android.com/design/style/iconography.html)):

| Type                      | Prefix       | Example                     |
| --------------------------------| ----------------   | ---------------------------- |
| Icons                           | `ic_`              | `ic_star.png`                |
| Launcher icons                  | `ic_launcher`      | `ic_launcher_calendar.png`   |
| Menu icons and Action Bar icons | `ic_menu`          | `ic_menu_share.png`        |
| Status bar icons                | `ic_stat_notify`   | `ic_stat_notify_msg.png`     |
| Tab icons                       | `ic_tab`           | `ic_tab_recent.png`          |
| Dialog icons                    | `ic_dialog`        | `ic_dialog_info.png`         |

Naming selector states:

| State	       | Suffix     | Example                     |
|--------------|-----------------|-----------------------------|
| Normal       | `_normal`       | `btn_xxx_normal.9.png`    |
| Pressed      | `_pressed`      | `btn_xxx_pressed.9.png`   |
| Focused      | `_focused`      | `btn_xxx_focused.9.png`   |
| Disabled     | `_disabled`     | `btn_xxx_disabled.9.png`  |
| Selected     | `_selected`     | `btn_xxx_selected.9.png`  |


#### 1.2.2 Layout files

Apply Type _object _ purpose structure. Layout file names should match the name of Android component, and put at the beginning of the Files. For example, file layout name of `LoginActivity` will be `activity_login.xml`.

| Component        | Class name             | Layout name                 |
| ---------------- | ---------------------- | ----------------------------- |
| Activity         | `LoginActivity`        | `activity_login.xml`   |
| Fragment         | `HomeFragment`         | `fragment_home.xml`        |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
| AdapterView item | ---                    | `item_person.xml`             |
| Partial layout   | ---                    | `partial_stats_bar.xml`       |

#### 1.2.3 Menu files

Because menu doesn't include Android component, naming Menu should follow name of screen.
For example, in `MainActivity`, file menu name will be `menu_activity_main.xml`

Naming file should not include `menu` because files already is in `menu` folder.

#### 1.2.4 Values files

Resources files in valus should be in __plural__. For example `strings.xml`, `styles.xml`, `colors.xml`, `dimens.xml`, `attrs.xml`

#### 1.2.5 Navigation files

Naming file example `xxx_navigation.xml`