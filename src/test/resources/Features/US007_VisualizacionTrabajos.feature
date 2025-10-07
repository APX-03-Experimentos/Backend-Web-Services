@US007 @dashboard @trabajos @estudiante
Feature: Visualización de trabajos y fechas
  Como estudiante
  Quiero visualizar en un dashboard todos mis trabajos y fechas de entrega
  Para organizar mis actividades y priorizar las más urgentes

  Scenario Outline: Ver lista de trabajos por curso
    Given un estudiante en la página principal del curso "<curso>"
    When navega a la pestaña "Trabajos"
    Then el sistema muestra lista completa de tareas asignadas
    And muestra el trabajo "<trabajo>" con estado "<estado>" y fecha "<fecha>"

    Examples:
      | curso       | trabajo          | estado     | fecha       |
      | Matemáticas | Tarea Álgebra    | Pendiente  | 2024-12-15  |
      | Física      | Proyecto Lab     | En progreso| 2024-12-20  |
      | Química     | Quiz Semanal     | Entregado  | 2024-12-05  |

