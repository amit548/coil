@file:JvmName("-Requests")

package coil.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.ViewSizeResolver
import coil.target.ViewTarget

/** Used to resolve [ImageRequest.placeholder], [ImageRequest.error], and [ImageRequest.fallback]. */
internal fun ImageRequest.getDrawableCompat(
    drawable: Drawable?,
    @DrawableRes resId: Int?,
    default: Drawable?
): Drawable? {
    return when {
        drawable != null -> drawable
        resId != null -> if (resId == 0) null else context.getDrawableCompat(resId)
        else -> default
    }
}

/** Return 'true' if the request does not require the output image's size to match the requested dimensions exactly. */
internal val ImageRequest.allowInexactSize: Boolean
    get() = when (precision) {
        Precision.EXACT -> false
        Precision.INEXACT -> true
        Precision.AUTOMATIC -> run {
            // If both our target and size resolver reference the same ImageView, allow the
            // dimensions to be inexact as the ImageView will scale the output image automatically.
            if (target is ViewTarget<*> && target.view is ImageView &&
                sizeResolver is ViewSizeResolver<*> && sizeResolver.view === target.view) {
                return true
            }

            // Else, require the dimensions to be exact.
            return false
        }
    }
