@US002 @grupos @profesor
Feature: Creación de grupos
  Como profesor
  Quiero ser capaz de crear grupos para mis cursos en la plataforma
  Para organizar a mis estudiantes y compartir materiales específicos

  Scenario Outline: Crear nuevo grupo exitosamente
    Given un profesor autenticado en la plataforma educativa
    When selecciona la opción "Crear Nuevo Grupo" desde el menú principal
    And completa los campos obligatorios:
      | campo       | valor         |
      | nombre      | <nombre>      |
      | descripción | <descripcion> |
    And confirma la creación
    Then el sistema guarda el grupo en la base de datos
    And muestra mensaje de confirmación "<mensaje_exito>"

    Examples:
      | nombre            | descripcion                          | mensaje_exito             |
      | Matemáticas 1     | Grupo para curso básico              | Grupo creado exitosamente |
      | Física Lab        | Laboratorio de prácticas             | Grupo creado exitosamente |
      | Programación Web  | Desarrollo frontend y backend        | Grupo creado exitosamente |

