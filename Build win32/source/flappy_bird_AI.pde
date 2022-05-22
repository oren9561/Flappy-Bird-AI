//Global variables
PImage midFlap, downFlap, upFlap, background, start, lose, pipe, pipeDown, ground;
PFont f;
float[] floor;
int score, gap, closestPipe, gen = -1, highScore = 0;
Pipe[] p = new Pipe[5];
float startTime;
Population pop; 
Brain[] b;

void setup(){
  size(1024, 576);
  
  //loading images, variables and text
  
  gap = 426;                                      //control the difficulty by increasing/decreasing the gap between the pipes
  midFlap = loadImage("midFlap.png");
  upFlap = loadImage("upFlap.png");
  downFlap = loadImage("downFlap.png");
  background = loadImage("background1.png");
  lose = loadImage("lose.png");
  start = loadImage("start.png");
  pipe = loadImage("pipe.png");
  pipeDown = loadImage("pipeDown.png");
  ground = loadImage("ground.png");
  f = createFont("font.TTF", 70, true);
  textFont(f, 70);
  score = 0;
  closestPipe = 0;
  startTime = millis();
  gen++;
  
  //making the floor
  
  floor = new float[6];
  floor[0] = -336;
  for(int i=1; i<6; i++)
     floor[i] = floor[i-1] + 336;
     
  //making the pipes
  
  for(int i=0; i<5; i++){
     int rand = (int)random(-260, -32);
     p[i] = new Pipe(1200 + 250*i, rand, rand+gap, pipeDown, pipe);
  }
  
  //updating the brains to the current population's brains
  ////making the population of birds (size of population can be changed in the brackets of the function)
  
  if(gen==0 || highScore == 0) pop = new Population(100);
  b = pop.getBrains();

}

void draw(){
  loadBG();
  for(int i=0; i<pop.getSize(); i++){
     Bird bird = b[i].getBird();
     if(!bird.isDead()){
       bird.setDead(bird.checkDead(p));
       if(b[i].activation() && (millis()/100 - bird.getLastTimeJumped()/100) > 3) bird.jump();
       bird.update();
     }
  }
  updateClosestPipe();
  if(pop.allDead()){
   pop.evolve();
   if(score > highScore) highScore = score;
   setup();
  }
}

void loadBG(){
 background(background); 
 for(int i=0; i<5; i++){
   p[i].update();
 }
 for(int i=0;i<6;i++){
    if(i==0){
      image(ground, floor[i], 467); 
      floor[i] -= 2.5;
      if(floor[i]<-350) floor[i] = (int)floor[5] + 330;
    }
    else{
      image(ground, floor[i], 467); 
      floor[i] -= 2.5;
      if(floor[i]<-350) floor[i] = (int)floor[i-1] + 330;
    }
 }
 text(score, 484, 100);                     //current score
 textSize(30);
 text(highScore + " - HighScore", 8, 62);   //highest score
 text(gen + " - gen", 8, 30);               //current gen
 textSize(70);
}

void updateClosestPipe(){ //updates the index of the pipe which is currently the closest to the birds
  if(!p[closestPipe].isFresh()){
     if(closestPipe == 4) closestPipe = 0;
     else closestPipe++;
  }
}
