# Define the Java compiler
JAVAC = javac

# Define flags for the Java compiler
JAVAC_FLAGS = -d .

# Define the source files directory
SRC_DIR = .

# Define the source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Define the target files (class files)
CLASSES = $(SOURCES:.java=.class)

# Default target
all: $(CLASSES)

# Rule for compiling Java source files to class files
%.class: %.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Clean target
clean:
	$(RM) $(CLASSES)
