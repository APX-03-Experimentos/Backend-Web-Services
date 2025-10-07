@US001 @grupos @estudiante
Feature: Ingreso a grupos
  Como estudiante
  Quiero ser capaz de unirme a los grupos formados por mis profesores con facilidad
  Para poder acceder a los materiales y tareas de cada curso

  Scenario Outline: Unirse a grupo con código válido
    Given un estudiante en el menú de inicio de la plataforma
    And un código de grupo válido "<codigo>" proporcionado por su profesor
    When ingresa el código "<codigo>" en el campo "Código de grupo"
    And selecciona la opción "Unirse al grupo"
    Then el sistema valida el código
    And muestra un mensaje de confirmación exitosa
    And añade automáticamente al estudiante al grupo "<grupo>"

    Examples:
      | codigo   | grupo              |
      | MATH2024 | Matemáticas Avanzadas |
      | PHYS101  | Física Básica      |
      | CHEM001  | Química General    |
      | PROG505  | Programación Avanzada |

