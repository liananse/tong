package com.mobilepower.tong.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.mobilepower.tong.R;

/**
 * An indicator of progress, similar to Android's ProgressBar. Can be used in
 * 'spin mode' or 'increment mode'
 * 
 * @author Todd Davies
 *         <p/>
 *         Licensed under the Creative Commons Attribution 3.0 license see:
 *         http://creativecommons.org/licenses/by/3.0/
 */
public class ProgressWheel extends View {

	// Sizes (with defaults)
	private int layout_height = 0;
	private int layout_width = 0;

	// 半径
	private int fullRadius = 100;
	private int circleRadius = 80;
	private int barLength = 60;
	private int barWidth = 20;
	private int rimWidth = 20;
	private int textSize = 20;

	// 轮廓
	private float contourSize = 0;

	// Padding (with defaults)
	private int paddingTop = 10;
	private int paddingBottom = 10;
	private int paddingLeft = 10;
	private int paddingRight = 10;

	// Colors (with defaults)
	private int barColor = 0xAA000000;
	private int contourColor = 0xAA000000;
	private int circleColor = 0x00000000;
	private int rimColor = 0xAADDDDDD;
	private int textColor = 0xFF000000;

	// Paints
	private Paint barPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint rimPaint = new Paint();
	private Paint textPaint = new Paint();
	private Paint contourPaint = new Paint();

	// Rectangles
	private RectF rimCircleBounds = new RectF();
	private RectF barCircleBounds = new RectF();
	private RectF circleOuterContour = new RectF();
	private RectF circleInnerContour = new RectF();

	// Animation
	// The amount of pixels to move the bar by on each draw
	private int spinSpeed = 2;
	// The number of milliseconds to wait inbetween each draw
	private int delayMillis = 0;
	private Handler spinHandler = new Handler() {
		/**
		 * This is the code that will increment the progress variable and so
		 * spin the wheel
		 */
		@Override
		public void handleMessage(Message msg) {
			invalidate();
			if (isSpinning) {
				progress += spinSpeed;
				if (progress > 360) {
					progress = 0;
				}
				spinHandler.sendEmptyMessageDelayed(0, delayMillis);
			}
		}
	};
	int progress = 0;
	boolean isSpinning = false;

	// Other
	private String text = "";
	private String[] splitText = {};

	public ProgressWheel(Context context) {
		super(context);
	}

	public ProgressWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		parseAttributes(context.obtainStyledAttributes(attrs,
				R.styleable.ProgressWheel));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		// Share the dimensions
		layout_width = w;
		layout_height = h;

		setupBounds();
		setupPaints();
		invalidate();
	}

	private void setupBounds() {
		// Width should equal to Height, find the min value to steup the circle
		int minValue = Math.min(layout_width, layout_height);

		System.out.println("setupBounds " + layout_height + " " + layout_width);
		// Calc the Offset if needed
		int xOffset = layout_width - minValue;
		int yOffset = layout_height - minValue;

		this.paddingLeft = paddingLeft + xOffset / 2;
		this.paddingRight = paddingRight + xOffset / 2;
		this.paddingBottom = paddingBottom + yOffset / 2;
		this.paddingTop = paddingTop + yOffset / 2;

		barCircleBounds = new RectF(paddingLeft + barWidth / 2, paddingTop
				+ barWidth / 2, this.getLayoutParams().width - paddingRight
				- barWidth / 2, this.getLayoutParams().height - paddingBottom
				- barWidth / 2);

		rimCircleBounds = new RectF(paddingLeft + rimWidth / 2, paddingTop
				+ rimWidth / 2, this.getLayoutParams().width - paddingRight
				- rimWidth / 2, this.getLayoutParams().height - paddingBottom
				- rimWidth / 2);

		circleInnerContour = new RectF(rimCircleBounds.left + (rimWidth / 2.0f)
				+ (contourSize / 2.0f), rimCircleBounds.top + (rimWidth / 2.0f)
				+ (contourSize / 2.0f), rimCircleBounds.right
				- (rimWidth / 2.0f) - (contourSize / 2.0f),
				rimCircleBounds.bottom - (rimWidth / 2.0f)
						- (contourSize / 2.0f));
		circleOuterContour = new RectF(rimCircleBounds.left - (rimWidth / 2.0f)
				- (contourSize / 2.0f), rimCircleBounds.top - (rimWidth / 2.0f)
				- (contourSize / 2.0f), rimCircleBounds.right
				+ (rimWidth / 2.0f) + (contourSize / 2.0f),
				rimCircleBounds.bottom + (rimWidth / 2.0f)
						+ (contourSize / 2.0f));

		fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
		circleRadius = (fullRadius - barWidth) + 1;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawArc(rimCircleBounds, 360, 360, false, rimPaint);
		canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
		canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
		// Draw the bar
		if (isSpinning) {
			canvas.drawArc(barCircleBounds, progress - 90, barLength, false,
					barPaint);
		} else {
			canvas.drawArc(barCircleBounds, -90, progress, false, barPaint);
		}
		// Draw the inner circle
		canvas.drawCircle((rimCircleBounds.width() / 2) + rimWidth
				+ paddingLeft, (rimCircleBounds.height() / 2) + rimWidth
				+ paddingTop, circleRadius, circlePaint);
		// Draw the text (attempts to center it horizontally and vertically)
		float textHeight = textPaint.descent() - textPaint.ascent();
		float verticalTextOffset = (textHeight / 2) - textPaint.descent();

		float length = 0.5f;
		if (splitText.length % 2 == 0) {
			length = -splitText.length / 2;
		} else {
			length = -(0.5f * splitText.length / 2);
		}
		for (int i = 0; i < splitText.length; i++) {
			String s = splitText[i];
			if (splitText.length > 1) {
				verticalTextOffset = length * (i + 1) * textHeight;
				length++;
			} else if (splitText.length == 1) {
				verticalTextOffset = (textHeight / 2) - textPaint.descent();
			}
			float horizontalTextOffset = textPaint.measureText(s) / 2;
			canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
					this.getHeight() / 2 + verticalTextOffset, textPaint);
		}
	}

	/**
	 * Set the properties of the paints we're using to draw the progress wheel
	 */
	private void setupPaints() {
		// bar画笔
		barPaint.setColor(barColor);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth);

		// 边画笔
		rimPaint.setColor(rimColor);
		rimPaint.setAntiAlias(true);
		rimPaint.setStyle(Style.STROKE);
		rimPaint.setStrokeWidth(rimWidth);

		// 貌似用不到
		circlePaint.setColor(circleColor);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.FILL);

		// 文字画笔
		textPaint.setColor(textColor);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);

		// 轮廓画笔
		contourPaint.setColor(contourColor);
		contourPaint.setAntiAlias(true);
		contourPaint.setStyle(Style.STROKE);
		contourPaint.setStrokeWidth(contourSize);
	}

	/**
	 * Parse the attributes passed to the view from the XML
	 * 
	 * @param a
	 *            the attributes to parse
	 */
	private void parseAttributes(TypedArray a) {
		barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
				barWidth);
		
		if (barWidth == 0) {
			this.paddingBottom = 0;
			this.paddingLeft = 0;
			this.paddingRight = 0;
			this.paddingTop = 0;
		}

		rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
				rimWidth);

		spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
				spinSpeed);

		delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis,
				delayMillis);
		if (delayMillis < 0) {
			delayMillis = 0;
		}

		barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

		barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength,
				barLength);

		textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
				textSize);

		textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor,
				textColor);

		// if the text is empty , so ignore it
		if (a.hasValue(R.styleable.ProgressWheel_text)) {
			setText(a.getString(R.styleable.ProgressWheel_text));
		}

		rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor,
				rimColor);

		circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor,
				circleColor);

		contourColor = a.getColor(R.styleable.ProgressWheel_contourColor,
				contourColor);
		contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize,
				contourSize);

		// Recycle
		a.recycle();
	}

	/**
	 * Reset the count (in increment mode)
	 */
	public void resetCount() {
		progress = 0;
		setText("0%");
		invalidate();
	}

	/**
	 * Turn off spin mode
	 */
	public void stopSpinning() {
		isSpinning = false;
		progress = 0;
		spinHandler.removeMessages(0);
	}

	/**
	 * Puts the view on spin mode
	 */
	public void spin() {
		isSpinning = true;
		spinHandler.sendEmptyMessage(0);
	}

	/**
	 * Increment the progress by 1 (of 360)
	 */
	public void incrementProgress() {
		isSpinning = false;
		progress++;
		if (progress > 360)
			progress = 0;
		spinHandler.sendEmptyMessage(0);
	}

	/**
	 * Set the progress to a specific value
	 */
	public void setProgress(int i) {
		isSpinning = false;
		progress = i;
		spinHandler.sendEmptyMessage(0);
	}

	// ----------------------------------
	// Getters + setters
	// ----------------------------------

	/**
	 * Set the text in the progress bar Doesn't invalidate the view
	 * 
	 * @param text
	 *            the text to show ('\n' constitutes a new line)
	 */
	public void setText(String text) {
		this.text = text;
		splitText = this.text.split("\n");
	}

	public int getCircleRadius() {
		return circleRadius;
	}

	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
	}

	public int getBarLength() {
		return barLength;
	}

	public void setBarLength(int barLength) {
		this.barLength = barLength;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getBarColor() {
		return barColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}
	
	public int getContourColor() {
		return contourColor;
	}

	public void setContourColor(int contourColor) {
		this.contourColor = contourColor;
	}

	public int getCircleColor() {
		return circleColor;
	}

	public void setCircleColor(int circleColor) {
		this.circleColor = circleColor;
	}

	public int getRimColor() {
		return rimColor;
	}

	public void setRimColor(int rimColor) {
		this.rimColor = rimColor;
	}

	public Shader getRimShader() {
		return rimPaint.getShader();
	}

	public void setRimShader(Shader shader) {
		this.rimPaint.setShader(shader);
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getSpinSpeed() {
		return spinSpeed;
	}

	public void setSpinSpeed(int spinSpeed) {
		this.spinSpeed = spinSpeed;
	}

	public int getRimWidth() {
		return rimWidth;
	}

	public void setRimWidth(int rimWidth) {
		this.rimWidth = rimWidth;
	}

	public int getDelayMillis() {
		return delayMillis;
	}

	public void setDelayMillis(int delayMillis) {
		this.delayMillis = delayMillis;
	}
}