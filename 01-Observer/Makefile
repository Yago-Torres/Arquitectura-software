# Compilador y flags
CXX = g++ 
# -Iinclude sirve para que el compilador busque en "include" los archivos a incluir, nos libramos de rutas absolutas.
# -Wall y -Wextra sirven para que el compilador nos muestre todos los warnings posibles, que viene bien, no necesario.
CXXFLAGS = -std=c++11 -Iinclude -Wall -Wextra

# Directorios
SRC_DIR = src
OBJ_DIR = obj
INC_DIR = include
TARGET = main

TEST = 1

# Archivos fuente y objetos.
# Wildcard sirve para que el makefile busque todos los cpp en src. Por eso se llama wildcard.
# Patsubst sirve para que el makefile reemplace el src por obj en los nombres de archivos.
SOURCES = $(wildcard $(SRC_DIR)/*.cpp)
OBJECTS = $(patsubst $(SRC_DIR)/%.cpp, $(OBJ_DIR)/%.o, $(SOURCES))


# Añadir test o no al compilador
ifeq ($(TEST), 1)
    CXXFLAGS += -DTEST  # Define TEST flag in the compiler for test mode
else
    CXXFLAGS += -DREAL  # Optionally, define REAL flag for normal mode
endif



# Regla principal: compilar todo
all: $(TARGET)

# Regla para enlazar el ejecutable
# $^ significa todos los prerequisitos de la regla, en este caso, todos los objetos.
# $@ significa el target de la regla, en este caso, el ejecutable.
# -lcurl sirve para que el compilador busque la librería curl que usamos.

$(TARGET): $(OBJECTS)
	$(CXX) $(CXXFLAGS) -o $@ $^ -lcurl

# Regla genérica para compilar .cpp -> .o
# $< significa el primer prerequisito de la regla, en este caso, el archivo .cpp.
# $@ significa el target de la regla, en este caso, el archivo .o.
$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp
	mkdir -p $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Regla para limpiar los archivos generados
clean:
	rm -rf $(OBJ_DIR) $(TARGET)
