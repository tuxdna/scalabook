/*
 * Taken from https://programmers.stackexchange.com/questions/131492/functional-programming-approach-for-a-simplified-game-using-scala-and-lwjgl/131505#131505
 */

package com.example.gaming;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import static java.awt.event.KeyEvent.*;

// An immutable wrapper around a Set. Doesn't implement Set or Collection
// because that would require quite a bit of code.
class ImmutableSet<T> implements Iterable<T> {
	final Set<T> backingSet;

	// Construct an empty set.
	ImmutableSet() {
		backingSet = new HashSet<T>();
	}

	// Copy constructor.
	ImmutableSet(ImmutableSet<T> src) {
		backingSet = new HashSet<T>(src.backingSet);
	}

	// Return a new set with an element added.
	ImmutableSet<T> plus(T elem) {
		ImmutableSet<T> copy = new ImmutableSet<T>(this);
		copy.backingSet.add(elem);
		return copy;
	}

	// Return a new set with an element removed.
	ImmutableSet<T> minus(T elem) {
		ImmutableSet<T> copy = new ImmutableSet<T>(this);
		copy.backingSet.remove(elem);
		return copy;
	}

	boolean contains(T elem) {
		return backingSet.contains(elem);
	}

	@Override
	public Iterator<T> iterator() {
		return backingSet.iterator();
	}
}

// An immutable, copy-on-write wrapper around BufferedImage.
class ImmutableImage {
	final BufferedImage backingImage;

	// Construct a blank image.
	ImmutableImage(int w, int h) {
		backingImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	// Copy constructor.
	ImmutableImage(ImmutableImage src) {
		backingImage = new BufferedImage(src.backingImage.getColorModel(),
				src.backingImage.copyData(null), false, null);
	}

	// Clear the image.
	ImmutableImage clear(Color c) {
		ImmutableImage copy = new ImmutableImage(this);
		Graphics g = copy.backingImage.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, backingImage.getWidth(), backingImage.getHeight());
		return copy;
	}

	// Draw a filled circle.
	ImmutableImage fillCircle(int x, int y, int r, Color c) {
		ImmutableImage copy = new ImmutableImage(this);
		Graphics g = copy.backingImage.getGraphics();
		g.setColor(c);
		g.fillOval(x - r, y - r, r * 2, r * 2);
		return copy;
	}
}

// An immutable, copy-on-write object describing the player.
class Player {
	final int x, y;
	final int ticksUntilFire;

	Player(int x, int y, int ticksUntilFire) {
		this.x = x;
		this.y = y;
		this.ticksUntilFire = ticksUntilFire;
	}

	// Construct a player at the starting position, ready to fire.
	Player() {
		this(SpaceInvaders.W / 2, SpaceInvaders.H - 50, 0);
	}

	// Update the game state (repeatedly called for each game tick).
	GameState update(GameState currentState) {
		// Update the player's position based on which keys are down.
		int newX = x;
		if (currentState.keyboard.isDown(VK_LEFT)
				|| currentState.keyboard.isDown(VK_A))
			newX -= 2;
		if (currentState.keyboard.isDown(VK_RIGHT)
				|| currentState.keyboard.isDown(VK_D))
			newX += 2;

		// Update the time until the player can fire.
		int newTicksUntilFire = ticksUntilFire;
		if (newTicksUntilFire > 0)
			--newTicksUntilFire;

		// Replace the old player with an updated player.
		Player newPlayer = new Player(newX, y, newTicksUntilFire);
		return currentState.setPlayer(newPlayer);
	}

	// Update the game state in response to a key press.
	GameState keyPressed(GameState currentState, int key) {
		if (key == VK_SPACE && ticksUntilFire == 0) {
			// Fire a bullet.
			Bullet b = new Bullet(x, y);
			ImmutableSet<Bullet> newBullets = currentState.bullets.plus(b);
			currentState = currentState.setBullets(newBullets);

			// Make the player wait 25 ticks before firing again.
			currentState = currentState.setPlayer(new Player(x, y, 25));
		}
		return currentState;
	}

	ImmutableImage render(ImmutableImage img) {
		return img.fillCircle(x, y, 20, Color.RED);
	}
}

// An immutable, copy-on-write object describing a bullet.
class Bullet {
	final int x, y;
	static final int radius = 5;

	Bullet(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Update the game state (repeatedly called for each game tick).
	GameState update(GameState currentState) {
		ImmutableSet<Bullet> bullets = currentState.bullets;
		bullets = bullets.minus(this);
		if (y + radius >= 0)
			// Add a copy of the bullet which has moved up the screen slightly.
			bullets = bullets.plus(new Bullet(x, y - 5));
		return currentState.setBullets(bullets);
	}

	ImmutableImage render(ImmutableImage img) {
		return img.fillCircle(x, y, radius, Color.BLACK);
	}
}

// An immutable, copy-on-write snapshot of the keyboard state at some time.
class KeyboardState {
	final ImmutableSet<Integer> depressedKeys;

	KeyboardState(ImmutableSet<Integer> depressedKeys) {
		this.depressedKeys = depressedKeys;
	}

	KeyboardState() {
		this(new ImmutableSet<Integer>());
	}

	GameState keyPressed(GameState currentState, int key) {
		return currentState.setKeyboard(new KeyboardState(depressedKeys
				.plus(key)));
	}

	GameState keyReleased(GameState currentState, int key) {
		return currentState.setKeyboard(new KeyboardState(depressedKeys
				.minus(key)));
	}

	boolean isDown(int key) {
		return depressedKeys.contains(key);
	}
}

// An immutable, copy-on-write description of the entire game state.
class GameState {
	final Player player;
	final ImmutableSet<Bullet> bullets;
	final KeyboardState keyboard;

	GameState(Player player, ImmutableSet<Bullet> bullets,
			KeyboardState keyboard) {
		this.player = player;
		this.bullets = bullets;
		this.keyboard = keyboard;
	}

	GameState() {
		this(new Player(), new ImmutableSet<Bullet>(), new KeyboardState());
	}

	GameState setPlayer(Player newPlayer) {
		return new GameState(newPlayer, bullets, keyboard);
	}

	GameState setBullets(ImmutableSet<Bullet> newBullets) {
		return new GameState(player, newBullets, keyboard);
	}

	GameState setKeyboard(KeyboardState newKeyboard) {
		return new GameState(player, bullets, newKeyboard);
	}

	// Update the game state (repeatedly called for each game tick).
	GameState update() {
		GameState current = this;
		current = current.player.update(current);
		for (Bullet b : current.bullets)
			current = b.update(current);
		return current;
	}

	// Update the game state in response to a key press.
	GameState keyPressed(int key) {
		GameState current = this;
		current = keyboard.keyPressed(current, key);
		current = player.keyPressed(current, key);
		return current;
	}

	// Update the game state in response to a key release.
	GameState keyReleased(int key) {
		GameState current = this;
		current = keyboard.keyReleased(current, key);
		return current;
	}

	ImmutableImage render() {
		ImmutableImage img = new ImmutableImage(SpaceInvaders.W,
				SpaceInvaders.H);
		img = img.clear(Color.BLUE);
		img = player.render(img);
		for (Bullet b : bullets)
			img = b.render(img);
		return img;
	}
}

public class SpaceInvaders {
	static final int W = 640, H = 480;

	static GameState currentState = new GameState();

	public static void main(String[] _) {
		JFrame frame = new JFrame() {
			{
				setSize(W, H);
				setTitle("Space Invaders");
				setContentPane(new JPanel() {
					@Override
					public void paintComponent(Graphics g) {
						BufferedImage img = SpaceInvaders.currentState.render().backingImage;
						((Graphics2D) g).drawRenderedImage(img,
								new AffineTransform());
					}
				});
				addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						currentState = currentState.keyPressed(e.getKeyCode());
					}

					@Override
					public void keyReleased(KeyEvent e) {
						currentState = currentState.keyReleased(e.getKeyCode());
					}
				});
				setLocationByPlatform(true);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
			}
		};

		for (;;) {
			currentState = currentState.update();
			frame.repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
}
