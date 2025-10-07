@US005 @trabajos @fechas @profesor
Feature: Creación de trabajos con fechas límite
  Como profesor
  Quiero crear trabajos dentro de cada curso con sus respectivas fechas de entrega
  Para organizar las actividades académicas y dar claridad a mis estudiantes

  Scenario Outline: Crear trabajo correctamente
    Given un profesor en la página de administración del curso "<curso>"
    When selecciona la opción "Crear trabajo"
    And completa los campos obligatorios:
      | campo        | valor               |
      | título       | <titulo>           |
      | descripción  | <descripcion>      |
      | fecha límite | <fecha_limite>     |
    And confirma la creación
    Then el sistema guarda el trabajo en la base de datos
    And muestra el trabajo en la lista de actividades del curso
    And notifica a todos los estudiantes del grupo

    Examples:
      | curso       | titulo          | descripcion               | fecha_limite |
      | Matemáticas | Tarea Álgebra   | Ejercicios páginas 1-10   | 2024-12-15   |
      | Física      | Proyecto Lab    | Informe de laboratorio    | 2024-12-20   |
      | Química     | Quiz Semanal    | Temas 1-5                 | 2024-12-10   |

  Scenario Outline: Error al ingresar fecha límite pasada
    Given un profesor en el proceso de creación de un nuevo trabajo
    When ingresa una fecha límite "<fecha_pasada>"
    And intenta guardar
    Then el sistema muestra mensaje de error "La fecha de entrega debe ser futura"
    And resalta el campo de fecha en rojo

    Examples:
      | fecha_pasada |
      | 2023-01-01   |
      | 2023-12-31   |
      | 2020-05-15   |




