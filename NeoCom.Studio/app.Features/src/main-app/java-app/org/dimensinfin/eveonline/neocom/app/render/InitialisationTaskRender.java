package org.dimensinfin.eveonline.neocom.app.render;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.InitialisationTaskController;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;

import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatterBuilder;

public class InitialisationTaskRender extends MVCRender {
	// - U I   F I E L D S
	private TextView taskName;
	private TextView status;
	private TextView elapsedTime;
	private ImageView spinnerOn;
	private ImageView spinnerOff;

	//	private Animation rotation;
	private CountDownTimer timer;

	public InitialisationTaskRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public InitialisationTaskController getController() {
		return (InitialisationTaskController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.initialisationtask4list;
	}

	@Override
	public void initializeViews() {
		taskName = this.getView().findViewById(R.id.taskName);
		status = this.getView().findViewById(R.id.status);
		elapsedTime = this.getView().findViewById(R.id.elapsedTime);
		spinnerOn = this.getView().findViewById(R.id.spinnerOn);
		spinnerOff = this.getView().findViewById(R.id.spinnerOff);
		spinnerOn.setVisibility(View.INVISIBLE);
		spinnerOff.setVisibility(View.VISIBLE);
//		this.initialiseTimeCounter();
	}

	@Override
	public void updateContent() {
		taskName.setText(this.getController().getModel().getTaskName());
		final InitialisationTask.ETaskStatus statusValue = this.getController().getModel().getStatus();
		logger.info("-- [InitialisationTaskRender.updateContent]> Status: {}", statusValue);
		status.setText(statusValue.name());
		if (statusValue == InitialisationTask.ETaskStatus.EXECUTING) {
			spinnerOn.setVisibility(View.VISIBLE);
			spinnerOff.setVisibility(View.INVISIBLE);
			this.initialiseTimeCounter();
		}
		if (statusValue == InitialisationTask.ETaskStatus.COMPLETED) {
			spinnerOn.setVisibility(View.INVISIBLE);
			spinnerOff.setVisibility(View.VISIBLE);
//			timer.cancel();
		}
		if (statusValue == InitialisationTask.ETaskStatus.EXCEPTION) {
			spinnerOn.setVisibility(View.INVISIBLE);
			spinnerOff.setVisibility(View.VISIBLE);
//			timer.cancel();
		}
	}

	private void initialiseTimeCounter() {
		final Instant _elapsedTimer = this.getController().getModel().getStartInstant();
		timer = new CountDownTimer(TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS.toMillis(10)) {
			@Override
			public void onFinish() {
				elapsedTime.setText(generateTimeString(_elapsedTimer.getMillis()));
				elapsedTime.invalidate();
			}

			@Override
			public void onTick( final long millisUntilFinished ) {
				elapsedTime.setText(generateTimeString(_elapsedTimer.getMillis()));
				elapsedTime.invalidate();
			}
		}.start();
	}

	private String generateTimeString( final long millis ) {
		try {
			final long elapsed = Instant.now().getMillis() - millis;
			final DateTimeFormatterBuilder timeFormatter = new DateTimeFormatterBuilder();
			if (elapsed > TimeUnit.HOURS.toMillis(1)) {
				timeFormatter.appendHourOfDay(2).appendLiteral("h ");
			}
			if (elapsed > TimeUnit.MINUTES.toMillis(1)) {
				timeFormatter.appendMinuteOfHour(2).appendLiteral("m ").appendSecondOfMinute(2).appendLiteral("s");
			} else timeFormatter.appendSecondOfMinute(2).appendLiteral("s");
			return timeFormatter.toFormatter().print(new Instant(elapsed));
		} catch (final RuntimeException rtex) {
			return "0m 00s";
		}
	}
}
