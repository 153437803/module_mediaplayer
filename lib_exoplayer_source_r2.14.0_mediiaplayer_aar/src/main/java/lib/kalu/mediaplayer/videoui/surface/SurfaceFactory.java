/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package lib.kalu.mediaplayer.videoui.surface;

import android.content.Context;

import androidx.annotation.Keep;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 扩展自己的渲染View
 *     revise: 可以使用TextureView，可参考{@link RenderTextureView}和{@link TextureViewFactory}的实现。
 * </pre>
 */
@Keep
public abstract class SurfaceFactory {

    public abstract InterSurfaceView createRenderView(Context context);

}
