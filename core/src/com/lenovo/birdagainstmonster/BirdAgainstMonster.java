package com.lenovo.birdagainstmonster;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.awt.geom.Arc2D;
import java.util.Random;

public class BirdAgainstMonster extends ApplicationAdapter
{
	//Tanımlamalar
	SpriteBatch batch;  //Sprite(nesne)'leri çizmemiz için yardımcı olan methodlar..
	Texture bg;         //Objenin resmini almak için Texture kullanırız...
	Texture bird;
	Texture monster1;
	Texture monster2;
	Texture monster3;

	//Oyun karakterlerinin koordinatları
	float birdX = 0;
	float birdY = 0;

	int gameState = 0; //Oyunun başlayıp başlamadığını anlamak için oluşturulan değişken
	float speed = 0;   //Bird için yere düşüş hızı
	float monsterSpeed = 2;
	float gravity = 0.1f; //speed değişkenine eklenen yer çekimi değişkeni...
	int score = 0;
	int scoredMonster = 0;

	Circle bird_Circle; //Karakterlerin çarpıştığını anlamamız için gerekli

	//ShapeRenderer shapeRenderer;

	int numberofMonster = 4;
	float[] monsterX = new float[numberofMonster];
	float[] monsterset1 = new float[numberofMonster];
	float[] monsterset2 = new float[numberofMonster];
	float[] monsterset3 = new float[numberofMonster];

	float distance = 0; //Monster karakterinin oyuna girişini ayarlamamız için gerekli deşişken...
	Random random;

	Circle[] monsterCircle1;
	Circle[] monsterCircle2;
	Circle[] monsterCircle3;

	BitmapFont font;   //Oyundaki yazı,skor gibi değerleri göstermek için kullanırız.
	BitmapFont font2;
	
	@Override
	public void create () //Uygulama açıldığında ne olacaksa buraya yazıyoruz...
	{
		//Yukarıda tanımladıklarımızı burada yaratıyoruz...
		batch = new SpriteBatch();
		bg = new Texture("background.png");
		bird = new Texture("bird.png");
		monster1 = new Texture("monster.png");
		monster2 = new Texture("monster.png");
		monster3 = new Texture("monster.png");

		//distance nesnesi uygulamanın çalıştığı ekranın genişliğinin yarısı kadar değer aldı...
		distance = Gdx.graphics.getWidth()/2;

		//shapeRenderer = new ShapeRenderer();

		random = new Random();

		//Bird karakterinin koordinatları...
		birdX = Gdx.graphics.getWidth()/3;
		birdY = Gdx.graphics.getHeight()/3;


		bird_Circle = new Circle();
		monsterCircle1 = new Circle[numberofMonster];
		monsterCircle2 = new Circle[numberofMonster];
		monsterCircle3 = new Circle[numberofMonster];

		font = new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(5); //Fontun büyüklüğü


		font2 = new BitmapFont();
		font2.setColor(Color.BLACK);
		font2.getData().setScale(6);


		for (int i=0 ; i<numberofMonster ; i++)
		{
			monsterX[i] = Gdx.graphics.getWidth() - monster1.getWidth() / 2 + i * distance;  //Her yeni gelen "monster" grubunun X eksenindeki yeri atandı.

			// "monster" grubundaki her karakterin Y eksenindeki yerleri ayarlandı...
			monsterset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			monsterset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			monsterset3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

			monsterCircle1[i] = new Circle();
			monsterCircle2[i] = new Circle();
			monsterCircle3[i] = new Circle();

		}
	}

	@Override
	public void render ()  //Oyun devam ettiği sürece devamlı olacak şeyler burada yazılır...
	{
		batch.begin();

		batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());  //Arka plan resmi eklendi...
		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/12); //Bird nesnesi yaratıldı...
		font.draw(batch,String.valueOf(score),100,200);  //Score değişkeninin sürekli yazıldığı font çizildi...

		if(gameState==1)  //Oyun başladıysa
		{
			if(monsterX[scoredMonster]<birdX)  //Eğer bird karakteri monster'lara çarpmadıysa ve onları geride bıraktıysa
			{
				score++;
				if(scoredMonster<numberofMonster-1)  //Monster grubunun tekrarlanması.4 set olduğu için
				{
					scoredMonster++;
				}
				else
				{
					scoredMonster=0;
				}
			}

			if(Gdx.input.justTouched()) //Oyun başladığı anda tekrar tıklanırsa
			{
				if(birdY < Gdx.graphics.getHeight())  //Bird Y ekseninin içinde kalıyorsa
				{
					speed -= 8;  //Yere düşüş hızını azalt ve bu sayede yükselmesini sağla...
				}
			}

			for (int i=0; i<numberofMonster ; i++)
			{
				if(monsterX[i]<0)
				{
					monsterX[i] = monsterX[i] + numberofMonster*distance;

					monsterset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					monsterset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					monsterset3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
				}
				else
				{
					monsterX[i] = monsterX[i] - monsterSpeed;
				}


				//Random şekilde gelen değerlerle birlikte Monster'ların yaratılması işlemi...
				batch.draw(monster1,monsterX[i],Gdx.graphics.getHeight()/2 + monsterset1[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/12);
				batch.draw(monster2,monsterX[i],Gdx.graphics.getHeight()/2 + monsterset2[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/12);
				batch.draw(monster3,monsterX[i],Gdx.graphics.getHeight()/2 + monsterset3[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/12);


				monsterCircle1[i] = new Circle(monsterX[i] + Gdx.graphics.getWidth()/24 , Gdx.graphics.getHeight()/2 + monsterset1[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);
				monsterCircle2[i] = new Circle(monsterX[i] + Gdx.graphics.getWidth()/24 , Gdx.graphics.getHeight()/2 + monsterset2[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);
				monsterCircle3[i] = new Circle(monsterX[i] + Gdx.graphics.getWidth()/24 , Gdx.graphics.getHeight()/2 + monsterset3[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);



			}

			if(birdY>0)  //Bird karakteri yerde değilse...
			{
				speed += gravity;
				birdY -= speed;
			}
			else
			{
				gameState=2;  //Oyunu bitirir.
			}
		}
		else if(gameState==0) //Oyun ekranı ilk açıldığında...
		{
			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}

		else if(gameState==2)  //Oyun bittiğinde...
		{
			font2.draw(batch,"Game Over! Tap to Play Again!",100,Gdx.graphics.getHeight()/2);
			if(Gdx.input.justTouched())
			{
				gameState=1;
				birdY = Gdx.graphics.getHeight()/3; //Oyuna tekrardan başlanırken Bird karakteri ilk yerinden başlar...


				//Tekrardan Monster'ların X ve Y koordinatları için atama yapılır...
				for (int i=0 ; i<numberofMonster ; i++)
				{
					monsterX[i] = Gdx.graphics.getWidth() - monster1.getWidth() / 2 + i * distance;

					monsterset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					monsterset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					monsterset3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

					monsterCircle1[i] = new Circle();
					monsterCircle2[i] = new Circle();
					monsterCircle3[i] = new Circle();

				}

				speed = 0;
				scoredMonster = 0;
				score = 0;
			}
		}


		//batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/12);
		bird_Circle.set(birdX+Gdx.graphics.getWidth()/20,birdY+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/36);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(bird_Circle.x,bird_Circle.y,bird_Circle.radius);*/

		for (int i=0;i<numberofMonster;i++)
		{
			/*shapeRenderer.circle(monsterX[i] + Gdx.graphics.getWidth()/24 , Gdx.graphics.getHeight()/2 + monsterset1[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);
			shapeRenderer.circle(monsterX[i] + Gdx.graphics.getWidth()/24 ,Gdx.graphics.getHeight()/2+ monsterset2[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);
			shapeRenderer.circle(monsterX[i] + Gdx.graphics.getWidth()/24 , Gdx.graphics.getHeight()/2 + monsterset3[i]+Gdx.graphics.getHeight()/24,Gdx.graphics.getWidth()/24);*/


			//Eğer Bird ve Monster'lardan biri çarpışırsa...
			if(Intersector.overlaps(bird_Circle,monsterCircle1[i]) || Intersector.overlaps(bird_Circle,monsterCircle2[i]) || Intersector.overlaps(bird_Circle,monsterCircle3[i]))
			{
				gameState=2;
			}


		}


//		shapeRenderer.end();

		batch.end();
	}
	
	@Override
	public void dispose ()
	{

	}
}
