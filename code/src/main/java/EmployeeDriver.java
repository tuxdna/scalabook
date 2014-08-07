import java.util.ArrayList;
import java.util.List;

public class EmployeeDriver {

	public static void main(String[] args) {
		List<Employee> employees = new ArrayList<Employee>();

		employees.add(new Employee("Saleem", "Ansari", 10, "Engineer", 10.0f,
				"Java"));
		employees.add(new Employee("Shalini", "Sharma", 11, "Engineer", 111.0f,
				".Net"));
		employees.add(new Employee("Varun", "Gupta", 12, "Engineer", 211.0f,
				"Java"));

		// Collections.sort (employees, new Comparator<Employee>(Empoyee e1,
		// Employee e2) {
		//
		// });

	}
}
