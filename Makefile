JC = JAVAC
JCR = java

.SUFFIXES: .java .class
.java.class:
        $(JC) $*.java

CLASSES = \
        Client.java

default: $(CLASSES:.java=.class)

clean:
        $(RM) *.class