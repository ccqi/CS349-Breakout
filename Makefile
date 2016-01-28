JFLAGS = -g -cp breakout/*
JC = javac
.SUFFIXES: .java .class
.java.class:
				$(JC) $(JFLAGS) $*.java

CLASSES = \
        breakout/Ball.java \
        breakout/Block.java \
        breakout/Breakout.java \
				breakout/BreakoutObj.java \
        breakout/Paddle.java 

default: clean classes

run: default 
				java breakout.Breakout ${fps} ${speed}
				

classes: $(CLASSES:.java=.class)

clean:
				$(RM) breakout/*.class
