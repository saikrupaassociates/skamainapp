package com.saikrupa.app.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.ClockType;
import com.alee.extended.time.WebClock;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

public class BaseAppDialog extends WebDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaseAppDialog(SKAMainApp owner) {
		super(owner, true);
		setResizable(false);
		setLocationRelativeTo(owner);
		setShowResizeCorner(false);
		addEscapeListener();
	}
	
	public BaseAppDialog(SKAMainApp owner, boolean model) {
		this(owner);
	}

	public BaseAppDialog() {
		addEscapeListener();
	}

	public BaseAppDialog(Frame owner) {
		super(owner);
	}

	public BaseAppDialog(Dialog owner) {
		super(owner, true);
		setResizable(false);
		setLocationRelativeTo(owner);
		setShowResizeCorner(false);
		addEscapeListener();
	}

	public BaseAppDialog(Component owner) {
		super(owner);
		addEscapeListener();
	}

	public BaseAppDialog(Window owner) {
		super(owner);
		addEscapeListener();
	}

	public BaseAppDialog(Frame owner, boolean modal) {
		super(owner, modal);
		addEscapeListener();
	}

	public BaseAppDialog(Frame owner, String title) {
		super(owner, title);
		addEscapeListener();
	}

	public BaseAppDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		addEscapeListener();
	}

	public BaseAppDialog(Dialog owner, String title) {
		super(owner, title);
		addEscapeListener();
	}

	public BaseAppDialog(Component owner, String title) {
		super(owner, title);
		addEscapeListener();
	}

	public BaseAppDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		addEscapeListener();
	}

	public BaseAppDialog(Window owner, String title) {
		super(owner, title);
		addEscapeListener();
	}

	public BaseAppDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		// addEscapeListener();
	}

	public BaseAppDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		// addEscapeListener();
	}

	public BaseAppDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		// addEscapeListener();
	}

	public BaseAppDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// addEscapeListener();
	}

	public BaseAppDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	public BaseAppDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		// TODO Auto-generated constructor stub
	}

	public void showSuccessNotification() {
		final WebNotification notificationPopup = new WebNotification();
		notificationPopup.setIcon(NotificationIcon.plus);
		notificationPopup.setDisplayTime(3000);

		final WebClock clock = new WebClock();
		clock.setClockType(ClockType.timer);
		clock.setTimeLeft(3000);
		clock.setTimePattern("'Operation Successfully executed !!!' ");
		clock.setFont(new Font("Verdana", Font.BOLD, 14));
		clock.setForeground(Color.BLACK);
		notificationPopup.setContent(new GroupPanel(clock));
		NotificationManager.showNotification(notificationPopup);
		clock.start();
	}

	public void showFailureNotification() {
		final WebNotification notificationPopup = new WebNotification();
		notificationPopup.setIcon(NotificationIcon.plus);
		notificationPopup.setDisplayTime(3000);

		final WebClock clock = new WebClock();
		clock.setClockType(ClockType.timer);
		clock.setTimeLeft(3000);
		clock.setTimePattern("'Operation was not Successful' ");
		clock.setFont(new Font("Verdana", Font.BOLD, 14));
		clock.setForeground(Color.RED);
		notificationPopup.setContent(new GroupPanel(clock));
		NotificationManager.showNotification(notificationPopup);
		clock.start();
	}

	public Icon createImageIcon(String path) {
		this.getClass().getClassLoader();
		java.net.URL imgURL = ClassLoader.getSystemResource("icons/" + path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file:  --> " + path);
			return null;
		}
	}

	public Font applyLabelFont() {
		return new Font("verdana", Font.BOLD, 13);
	}

	public Font applyTableFont() {
		return new Font("verdana", Font.BOLD, 13);
	}

	private void addEscapeListener() {
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		getRootPane().registerKeyboardAction(listener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

}
