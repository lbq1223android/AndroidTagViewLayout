# Android TagViewLayout

Do you want to make your image more attractive and straightforward? Why not trying this fascinating TagViewLayout for Android platform? You can simply put tags on an image by just providing their locations, directions and of course texts.

## Demo
![](https://raw.githubusercontent.com/ZhouShengsheng/AndroidTagViewLayout/master/app/src/main/res/raw/tagViewLayout_demo.gif)

[Download Apk](https://github.com/ZhouShengsheng/AndroidTagViewLayout/blob/master/app/build/outputs/apk/app-debug.apk?raw=true)

## Contents
- [Introduction](#introduction)
- [TagView](#tagview)
- [TagViewLayout](#tagviewlayout)
- [Usage](#usage)
- [License](#license)

## Introduction
The [TagViewLayout](#tagViewLayout) library allows you add tagViews on a TagViewLayout. A [TagView](#tagView) contains an animated point, a folded line and a text tag shown as the above. You can also define the [Direction](#direction) of each tagView. This library lets your image become more full of meaning.

## TagView
The TagView is inherited from FrameLayout. A TagView contains an animated point, a folded line and a text tag. Each tagView has four different directions, LEFT_TOP, LEFT_DOWN, RIGHT_TOP and RIGHT_DOWN. The animated point shows animations in repeated mode. You can stop the animations if you don't like them. And the text tag shows your text with the background of a tag.

## TagViewLayout
The TagViewLayout is subclass of RelativeLayout. It's the container of tagViews. You can simply add tagViews on a tagViewLayout by calling it's addTags() method.

## Usage
### step 1
Clone this project into your computer and set up the environment of the project. Run the project to see what's going on.
### step 2
Copy the TagViewLayout directory into your own project directory and add the library to your project. Make sure that you have called DensityHelper.init(activity) before you setTags for tagViewLayout.
```java
// better call in a SplashActivity which is the first activity of the project
DensityHelper.init(activity);
```  
### step3
Then add TagViewLayout into your layout.
```java
<ntu.zss.tagviewlayout.TagViewLayout
        android:id="@+id/tagViewLayout"
        android:layout_width="match_parent"
        android:layout_height="240dp" />
```  
### step 4
Create some tagViews and add them to tagViewLayout.
```java
private void initTags()
{
    tags = new ArrayList<>();
    // add some tags for testing purpose
    tags.add(new TagItem(DensityHelper.dp2px(28), DensityHelper.dp2px(162),
            getString(R.string.tag_text_tag1), TagItem.Direction.RIGHT_TOP));
    tags.add(new TagItem(DensityHelper.dp2px(230), DensityHelper.dp2px(50),
            getString(R.string.tag_text_tag2), TagItem.Direction.LEFT_TOP));
    tags.add(new TagItem(DensityHelper.dp2px(180), DensityHelper.dp2px(160),
            getString(R.string.tag_text_tag3), TagItem.Direction.RIGHT_BOTTOM));
}

private void displayTags()
{
    tagViewLayout.setTags(tags);
    tagViewLayout.displayTags();
    // set onTagClickListener for the tagViewLayout
    tagViewLayout.setOnTagClickListener(new TagViewLayout.OnTagClickListener()
    {
        @Override
        public void onTagClick(TagView tagView)
        {
            Toast.makeText(MainActivity.this, tagView.tag.text, Toast.LENGTH_SHORT).show();
        }
    });
}
```  

## License
    Copyright 2015 NTU (http://www.ntu.edu.sg/)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.