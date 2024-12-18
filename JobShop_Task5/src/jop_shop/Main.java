package jop_shop;

import jop_shop.dao.*;
import jop_shop.model.Process;
import jop_shop.model.*;

import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static CustomerDao customerDao = new CustomerDao();
    private static DepartmentDao departmentDao = new DepartmentDao();
    private static ProcessDao processDao = new ProcessDao();
    private static JobDao jobDao = new JobDao();
    private static AccountDao accountDao = new AccountDao();
    private static AssemblyDao assemblyDao = new AssemblyDao();
    private static TransactionDao transactionDao = new TransactionDao();

    /**
     * View dashboard console, choose option
     * @param args
     */
    public static void main(String[] args) {
        Scanner keyboard = null;
        try {
            keyboard = new Scanner(System.in);
            while (true) {
                System.out.println("WELCOME TO THE JOB-SHOP ACCOUNTING DATABASE SYSTEM");
                System.out.println("1. Enter a new customer (30/day).");
                System.out.println("2. Enter a new department (infrequent).");
                System.out.println("3. Enter a new process-id and its department together with its type and information relevant to the type (infrequent).");
                System.out.println("4. Enter a new assembly with its customer-name, assembly-details, assembly-id, and date ordered and associate it with one or more processes (40/day).");
                System.out.println("5. Create a new account and associate it with the process, assembly, or department to which it is applicable (10/day).");
                System.out.println("6. Enter a new job, given its job-no, assembly-id, process-id, and date the job commenced (50/day).");
                System.out.println("7. At the completion of a job, enter the date it was completed and the information relevant to the type of job (50/day).");
                System.out.println("8. Enter a transaction-no and its sup-cost and update all the costs (details) of the affected accounts by adding sup-cost to their current values of details (50/day).");
                System.out.println("9. Retrieve the total cost incurred on an assembly-id (200/day).");
                System.out.println("10. Retrieve the total labor time within a department for jobs completed in the department during a given date (20/day).");
                System.out.println("11. Retrieve the processes through which a given assembly-id has passed so far (in date commenced order) and the department responsible for each process (100/day).");
                System.out.println("12. Retrieve the customers (in name order) whose category is in a given range (100/day).");
                System.out.println("13. Delete all cut jobs whose job-no is in a given range (1/month)");
                System.out.println("14. Change the color of a given paint job (1/week)");
                System.out.println("15. Import: enter new customers from a data file until the file is empty " +
                        "(the user must be asked to enter the input file name).");
                System.out.println("16. Export: Retrieve the customers (in name order) whose category is in a given " +
                        "range and output them to a data file instead of screen (the user must be asked to enter the output file name).");
                System.out.println("17. Exit");
                String choice = keyboard.nextLine();
                switch (choice) {
                    case "1":
                        handleOption1(keyboard);
                        break;
                    case "2":
                        handleOption2(keyboard);
                        break;
                    case "3":
                        handleOption3(keyboard);
                        break;
                    case "4":
                        handleOption4(keyboard);
                        break;
                    case "5":
                        handleOption5(keyboard);
                        break;
                    case "6":
                        handleOption6(keyboard);
                        break;
                    case "7":
                        handleOption7(keyboard);
                        break;
                    case "8":
                        handleOption8(keyboard);
                        break;
                    case "9":
                        handleOption9(keyboard);
                        break;
                    case "10":
                        handleOption10(keyboard);
                        break;
                    case "11":
                        handleOption11(keyboard);
                        break;
                    case "12":
                        handleOption12(keyboard);
                        break;
                    case "13":
                        handleOption13(keyboard);
                        break;
                    case "14":
                        handleOption14(keyboard);
                        break;
                    case "15":
                        handleOption15(keyboard);
                        break;
                    case "16":
                        handleOption16(keyboard);
                        break;
                    case "17":
                        System.out.println("The program has closed, thank you.");
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurs");
        } finally {
            if (keyboard != null) {
                keyboard.close();
            }
        }
    }

    /**
     * Handle option 16
     * @param keyboard
     */
    private static void handleOption16(Scanner keyboard) throws IOException {
        System.out.println("Enter an input file name (absolute path): ");
        String fileName = keyboard.nextLine();
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        String againMessage = "Enter the first category: ";
        System.out.println(againMessage);
        int categoryId1 = inputNumber(keyboard, againMessage);

        againMessage = "Enter the second category: ";
        System.out.println(againMessage);
        int categoryId2 = inputNumber(keyboard, againMessage);

        if (categoryId1 >= categoryId2) {
            System.out.println("The category id 1 is greater than the category id 2. Please try again");
            return;
        }

        List<Customer> customerList = customerDao.findAllByCategory(categoryId1, categoryId2);

        boolean result = exportCustomerToFile(file, customerList);
        if (result) {
            System.out.println("Export the customers to the file successfully");
        } else {
            System.out.println("Export the customers to the file fail");
        }
    }

    /**
     * Export list customer data to file
     * @param file
     * @param customerList
     * @return
     */
    private static boolean exportCustomerToFile(File file, List<Customer> customerList) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (Customer c : customerList) {
                String data = c.getCustomerId() + "," + c.getName() + "," + c.getAddress() + "," + c.getCategory() + "\r\n";
                bufferedWriter.write(data);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * Handle option 15
     * @param keyboard
     */
    private static void handleOption15(Scanner keyboard) {
        System.out.println("Enter an input file name (absolute path): ");
        String fileName = keyboard.nextLine();
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("The file name not exists ");
            return;
        }
        List<Customer> customerList = readImportCustomerFile(file);
        for (Customer customer : customerList) {
            customerDao.add(customer.getName(), customer.getAddress(), customer.getCategory());
        }
        System.out.println("Import the customers from the file successfully");
    }

    /**
     * Read a file for importing the customer
     * @param file
     * @return
     */
    private static List<Customer> readImportCustomerFile(File file) {
        List<Customer> customerList = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String address = data[1];
                int category = 0;
                try {
                    category = Integer.parseInt(data[2]);
                } catch (Exception e) {

                }
                Customer customer = new Customer(0, name, address, category);
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {

            }
        }
        return customerList;
    }

    /**
     * Handle option 14
     * @param keyboard
     */
    private static void handleOption14(Scanner keyboard) {
        System.out.println("Enter a paint job");
        List<Job> jobList = jobDao.findAll().stream().filter(j -> j instanceof PaintJob).collect(Collectors.toList());
        printJobData(jobList);
        Job job = inputJob(keyboard, jobList);
//        int jobNumber = inputNumber(keyboard, "Enter a paint job");
        System.out.println("Enter a change color: ");
        String color = keyboard.nextLine();

        boolean result = jobDao.updatePaintJobColor(job.getJobNumber(), color);
        if (result) {
            System.out.println("Change the color of the paint job successfully");
        } else {
            System.out.println("Change the color of the paint job fail");
        }
    }

    /**
     * Handle option 13
     * @param keyboard
     */
    private static void handleOption13(Scanner keyboard) {
        String againMessage = "Enter the first job number: ";
        System.out.println(againMessage);
        int jobNumber1 = inputNumber(keyboard, againMessage);

        againMessage = "Enter the second job number: ";
        System.out.println(againMessage);
        int jobNumber2 = inputNumber(keyboard, againMessage);

        if (jobNumber1 >= jobNumber2) {
            System.out.println("The job number 1 is greater than the job number 2. Please try again");
            return;
        }

        boolean result = jobDao.deleteJobWithRange(jobNumber1, jobNumber2);
        if (result) {
            System.out.println("Delete the jobs with range successfully");
        } else {
            System.out.println("Delete the jobs with range fail");
        }
    }

    /**
     * Handle option 12
     * @param keyboard
     */
    private static void handleOption12(Scanner keyboard) {
        String againMessage = "Enter the first category: ";
        System.out.println(againMessage);
        int categoryId1 = inputNumber(keyboard, againMessage);

        againMessage = "Enter the second category: ";
        System.out.println(againMessage);
        int categoryId2 = inputNumber(keyboard, againMessage);

        if (categoryId1 >= categoryId2) {
            System.out.println("The job number 1 is greater than the job number 2. Please try again");
            return;
        }

        List<Customer> customerList = customerDao.findAllByCategory(categoryId1, categoryId2);
        printCustomerData(customerList);
    }

    /**
     * Handle option 11
     * @param keyboard
     */
    private static void handleOption11(Scanner keyboard) {
        List<Assembly> assemblyList = assemblyDao.findAll();
        printAssemblyData(assemblyList);
        Assembly assembly = inputAssembly(keyboard, assemblyList);
        int assemblyId = assembly.getAssemblyId();

        List<Department> departmentList = departmentDao.findAll();

        List<Process> processList = processDao.finAllByAssemblyAndJobCompleted(assemblyId);
        System.out.println("All the processes and the department responsible for each process: ");
        for (Process process : processList) {
            String processType;
            if (process instanceof FitProcess) {
                processType = "Fit Process";
            } else if (process instanceof CutProcess) {
                processType = "Cut Process";
            } else {
                processType = "Paint Process";
            }
            Department department = departmentList.stream().filter(d -> d.getDepartmentId() == process.getDepartmentId()).findFirst().orElse(new Department());
            System.out.println("Process: " + process.getProcessId() + "---" + processType + "---" + process.getProcessData() + ", department: " + department.getDepartmentId() + "---" + department.getDepartmentData());
        }
    }

    /**
     * Handle option 10
     * @param keyboard
     */
    private static void handleOption10(Scanner keyboard) {
        List<Department> departmentList = departmentDao.findAll();
        printDepartmentData(departmentList);
        Department department = inputDepartment(keyboard, departmentList);

        String againMessage = "Enter a completed date (dd-MM-yyyy): ";
        System.out.println(againMessage);
        Date completedDate = inputDate(keyboard, againMessage);

        int totalLaborTime = jobDao.getTotalLaborTime(department.getDepartmentId(), completedDate);
        System.out.println("The total labor time within a department and during a given date is: " + totalLaborTime);
    }

    /**
     * Handle option 9
     * @param keyboard
     */
    private static void handleOption9(Scanner keyboard) {
        List<Assembly> assemblyList = assemblyDao.findAll();
        printAssemblyData(assemblyList);
        Assembly assembly = inputAssembly(keyboard, assemblyList);

        double totalCost = accountDao.getTotalCostByAssemblyId(assembly.getAssemblyId());
        System.out.println("The total cost incurred on an assembly-id " + assembly.getAssemblyId() + " is: " + totalCost);
    }

    /**
     * Handle option 8
     * @param keyboard
     */
    private static void handleOption8(Scanner keyboard) {
        List<Transaction> transactionList = transactionDao.findAll();
        printTransactionData(transactionList);
        Transaction transaction = inputTransaction(keyboard, transactionList);

        String againMessage = "Enter a supplied cost: ";
        System.out.println(againMessage);
        double suppliedCost = inputDecimal(keyboard, againMessage);

        boolean result = accountDao.updateSuppliedCost(transaction, suppliedCost);
        if (result) {
            System.out.println("Update the supplied cost successfully");
        } else {
            System.out.println("Update the supplied cost fail");
        }
    }

    /**
     * Handle option 7
     * @param keyboard
     */
    private static void handleOption7(Scanner keyboard) {
        System.out.println("You need to add a cost transaction before updating the completed date of the job");

        List<Account> accountList = accountDao.findAll();
        printAccountData(accountList);
        Account account = inputAccount(keyboard, accountList);

        String againMessage = "Enter a supplied cost: ";
        System.out.println(againMessage);
        double suppliedCost = inputDecimal(keyboard, againMessage);

        List<Job> jobList = jobDao.findAll();
        printJobData(jobList);
        Job job = inputJob(keyboard, jobList);
        int jobNumber = job.getJobNumber();

        Transaction transaction = new Transaction(suppliedCost, account.getAccountNumber(), jobNumber);

        againMessage = "Enter a completed date of the job (dd-MM-yyyy): ";
        System.out.println(againMessage);
        Date completedDate = inputDate(keyboard, againMessage);

        boolean result = jobDao.updateCompletedDateAndInsertTran(jobNumber, completedDate, transaction);
        if (result) {
            System.out.println("Update the completed date of the job successfully");
        } else {
            System.out.println("Update the completed date of the job fail");
        }
    }

    /**
     * Handle option 6
     * @param keyboard
     */
    private static void handleOption6(Scanner keyboard) {
        List<Assembly> assemblyList = assemblyDao.findAll();
        List<Process> processList = processDao.findAll();

        while (true) {
            System.out.println("Enter a new job");
            String jobType;
            while (true) {
                System.out.println("1. Fit job");
                System.out.println("2. Cut job");
                System.out.println("3. Paint job");
                System.out.println("Choose type job: ");
                jobType = keyboard.nextLine();
                if (!Arrays.asList("1,2,3".split(",")).contains(jobType)) {
                    System.out.println("Invalid job type, please try again");
                } else {
                    break;
                }
            }

            printAssemblyData(assemblyList);
            Assembly assembly = inputAssembly(keyboard, assemblyList);
            int assemblyId = assembly.getAssemblyId();

            if ("1".equals(jobType)) {
                processList = processList.stream().filter(f -> f instanceof FitProcess).collect(Collectors.toList());
            } else if ("2".equals(jobType)) {
                processList = processList.stream().filter(f -> f instanceof CutProcess).collect(Collectors.toList());
            } else if ("3".equals(jobType)) {
                processList = processList.stream().filter(f -> f instanceof PaintProcess).collect(Collectors.toList());
            }
            if (processList.isEmpty()) {
                System.out.println("Dont have properly processes, please add this properly process first");
                return;
            }

            printProcessData(processList);
            Process process = inputProcess(keyboard, processList);
            int processId = process.getProcessId();

            String againMessage = "Enter a commenced date (dd-MM-yyyy): ";
            System.out.println(againMessage);
            Date commendDate = inputDate(keyboard, againMessage);

            againMessage = "Enter a labor time (hours): ";
            System.out.println(againMessage);
            int laborTime = inputNumber(keyboard, againMessage);

            Job job = null;
            if ("1".equals(jobType)) {
                job = new FitJob(null, commendDate, assemblyId, processId, laborTime);
            } else if ("2".equals(jobType)) {
                System.out.println("Enter a machine type: ");
                String machineType = keyboard.nextLine();

                againMessage = "Enter a machine used time (hours): ";
                System.out.println(againMessage);
                int machineUsedTime = inputNumber(keyboard, againMessage);

                System.out.println("Enter a material used: ");
                String materialUsed = keyboard.nextLine();

                job = new CutJob(null, commendDate, assemblyId, processId, laborTime, machineType, machineUsedTime, materialUsed);
            } else if ("3".equals(jobType)) {
                System.out.println("Enter a color: ");
                String color = keyboard.nextLine();

                againMessage = "Enter a volume: ";
                System.out.println(againMessage);
                int volume = inputNumber(keyboard, againMessage);

                job = new PaintJob(null, commendDate, assemblyId, processId, laborTime, color, volume);
            }
            boolean result = jobDao.add(job);
            if (result) {
                System.out.println("Add the job successfully");
            } else {
                System.out.println("Add the job fail");
            }

            System.out.println("Do you need add more jobs? (Y/N)");
            String jobChoice = keyboard.nextLine();
            if ("N".equalsIgnoreCase(jobChoice)) {
                break;
            }
        }
    }

    /**
     * Handle option 5
     * @param keyboard
     */
    private static void handleOption5(Scanner keyboard) {
        System.out.println("Enter a new account");
        while (true) {
            System.out.println("1. Assembly account");
            System.out.println("2. Process account");
            System.out.println("3. Department account");
            System.out.println("Choose type account: ");
            String accountType = keyboard.nextLine();
            if (!Arrays.asList("1,2,3".split(",")).contains(accountType)) {
                System.out.println("Invalid account type, try enter again");
                continue;
            }

            String accountNo;
            System.out.println("Enter an account no: ");
            accountNo = keyboard.nextLine();
//            while (true) {
//                System.out.println("Enter an account no: ");
//                accountNo = keyboard.nextLine();
//                if (accountDao.checkExistAccountNo(accountNo)) {
//                    System.out.println("This account no have existed on the system, please enter again");
//                } else {
//                    break;
//                }
//            }

            String againName = "Enter a cost: ";
            System.out.println(againName);
            double cost = inputDecimal(keyboard, againName);

            Date establishedDate = new Date(new java.util.Date().getTime());
            Account account;
            if ("1".equals(accountType)) {
                List<Assembly> assemblyList = assemblyDao.findAll();
                printAssemblyData(assemblyList);
                Assembly assembly = inputAssembly(keyboard, assemblyList);
                int assemblyId = assembly.getAssemblyId();

                account = new AssemblyAccount(accountNo, establishedDate, cost, assemblyId);
            } else if ("2".equals(accountType)) {
                List<Process> processList = processDao.findAll();
                printProcessData(processList);
                Process process = inputProcess(keyboard, processList);
                int processId = process.getProcessId();
                account = new ProcessAccount(accountNo, establishedDate, cost, processId);
            } else if ("3".equals(accountType)) {
                List<Department> departmentList = departmentDao.findAll();
                printDepartmentData(departmentList);
                Department department = inputDepartment(keyboard, departmentList);
                int departmentId = department.getDepartmentId();

                account = new DepartmentAccount(accountNo, establishedDate, cost, departmentId);
            } else {
                System.out.println("Invalid account type");
                continue;
            }
            boolean result = accountDao.add(account);
            if (result) {
                System.out.println("Add account successfully");
            } else {
                System.out.println("Add account fail");
            }
            break;
        }

    }

    /**
     * Handle option 4
     * @param keyboard
     */
    private static void handleOption4(Scanner keyboard) {
        System.out.println("Enter a new assembly");

        System.out.println("Enter an assembly detail: ");
        String assemblyDetail = keyboard.nextLine();

        List<Customer> customerList = customerDao.findAll();
        printCustomerData(customerList);
        Customer customer = inputCustomer(keyboard, customerList);
        int customerId = customer.getCustomerId();

        Date orderDate = new Date(new java.util.Date().getTime());
        Assembly assembly = new Assembly(orderDate, assemblyDetail, customerId);
        int assemblyId = assemblyDao.add(assembly);

        List<Process> processList = processDao.findAll();
        while (true) {
            printProcessData(processList);
            Process process = inputProcess(keyboard, processList);
            int processId = process.getProcessId();
            while (true) {
                Job job = null;
                if (process instanceof FitProcess) {
                    System.out.println("Enter a fit job");

                    String againMessage = "Enter a commenced date (dd-MM-yyyy): ";
                    System.out.println(againMessage);
                    Date commendDate = inputDate(keyboard, againMessage);

                    againMessage = "Enter a labor time (hours): ";
                    System.out.println(againMessage);
                    int laborTime = inputNumber(keyboard, againMessage);

                    job = new FitJob(null, commendDate, processId, laborTime);
                } else if (process instanceof CutProcess) {
                    System.out.println("Enter a cut job");

                    String againMessage = "Enter a commenced date (dd-MM-yyyy): ";
                    System.out.println(againMessage);
                    Date commendDate = inputDate(keyboard, againMessage);

                    againMessage = "Enter a labor time (hours): ";
                    System.out.println(againMessage);
                    int laborTime = inputNumber(keyboard, againMessage);

                    System.out.println("Enter a machine type: ");
                    String machineType = keyboard.nextLine();

                    againMessage = "Enter a machine used time (hours): ";
                    System.out.println(againMessage);
                    int machineUsedTime = inputNumber(keyboard, againMessage);

                    System.out.println("Enter a material used: ");
                    String materialUsed = keyboard.nextLine();

                    job = new CutJob(null, commendDate, processId, laborTime, machineType, machineUsedTime, materialUsed);
                } else if (process instanceof PaintProcess) {
                    System.out.println("Enter a paint job");

                    String againMessage = "Enter a commenced date (dd-MM-yyyy): ";
                    System.out.println(againMessage);
                    Date commendDate = inputDate(keyboard, againMessage);

                    againMessage = "Enter a labor time (hours): ";
                    System.out.println(againMessage);
                    int laborTime = inputNumber(keyboard, againMessage);

                    System.out.println("Enter a color: ");
                    String color = keyboard.nextLine();

                    againMessage = "Enter a volume: ";
                    System.out.println(againMessage);
                    int volume = inputNumber(keyboard, againMessage);

                    job = new PaintJob(null, commendDate, processId, laborTime, color, volume);
                }
                if (job != null) {
                    job.setAssemblyId(assemblyId);
                    boolean result = jobDao.add(job);
                    if (result) {
                        System.out.println("Add the assembly and the job successfully");
                    } else {
                        System.out.println("Add the assembly and the job fail");
                    }

                    System.out.println("Do you need add more jobs for this process? (Y/N)");
                    String jobChoice = keyboard.nextLine();
                    if ("N".equalsIgnoreCase(jobChoice)) {
                        break;
                    }
                }
            }
            System.out.println("Do you need add more processes for this assembly? (Y/N)");
            String processChoice = keyboard.nextLine();
            if ("N".equalsIgnoreCase(processChoice)) {
                break;
            }
        }
    }

    /**
     * Handle option 3
     * @param keyboard
     */
    private static void handleOption3(Scanner keyboard) {
        String processType;
        while (true) {
            System.out.println("Choose type process: ");
            System.out.println("1. Fit process");
            System.out.println("2. Cut process");
            System.out.println("3. Paint process");
            processType = keyboard.nextLine();
            if (!Arrays.asList("1,2,3".split(",")).contains(processType)) {
                System.out.println("Invalid process type, try again");
            } else {
                break;
            }
        }

        List<Department> departmentList = departmentDao.findAll();
        printDepartmentData(departmentList);
//        Department department = inputDepartment(keyboard, departmentList);
//        int departmentId = department.getDepartmentId();
        System.out.println("Enter a denpartment id: ");
        int departmentId = inputNumber(keyboard, "Enter a denpartment id: ");
        System.out.println("Enter a process data: ");
        String processData = keyboard.nextLine();

        Process process;
        while (true) {
            if ("1".equals(processType)) {
                System.out.println("Enter a fit type: ");
                String fitType = keyboard.nextLine();
                process = new FitProcess(processData, departmentId, fitType);
                break;
            } else if ("2".equals(processType)) {
                System.out.println("Enter a cutting type: ");
                String cuttingType = keyboard.nextLine();

                System.out.println("Enter a machine type: ");
                String machineType = keyboard.nextLine();

                process = new CutProcess(processData, departmentId, cuttingType, machineType);
                break;
            } else if ("3".equals(processType)) {
                System.out.println("Enter a paint type: ");
                String paintType = keyboard.nextLine();

                System.out.println("Enter a paint method: ");
                String paintMethod = keyboard.nextLine();

                process = new PaintProcess(processData, departmentId, paintType, paintMethod);
                break;
            }
        }
        boolean result = processDao.add(process);
        if (result) {
            System.out.println("Add the process successfully");
        } else {
            System.out.println("Add the process fail");
        }
    }

    /**
     * Handle option 2
     * @param keyboard
     */
    private static void handleOption2(Scanner keyboard) {
        System.out.println("Department data: ");
        String departmentData = keyboard.nextLine();
        boolean result = departmentDao.add(departmentData);
        if (result) {
            System.out.println("Add the department successfully");
        } else {
            System.out.println("Add the department fail");
        }
    }

    /**
     * Handle option 1
     * @param keyboard
     */
    private static void handleOption1(Scanner keyboard) {
        System.out.println("Name: ");
        String name = keyboard.nextLine();
        System.out.println("Address: ");
        String address = keyboard.nextLine();

        String againMessage = "Enter a category: ";
        System.out.println(againMessage);
        int category = inputNumber(keyboard, againMessage);
        boolean result = customerDao.add(name, address, category);
        if (result) {
            System.out.println("Add the customer successfully");
        } else {
            System.out.println("Add the customer fail");
        }
    }

    /**
     * Enter a decimal number and validate it
     * @param keyboard
     * @param againName
     * @return
     */
    private static double inputDecimal(Scanner keyboard, String againName) {
        while (true) {
            try {
                String costStr = keyboard.nextLine();
                return Double.parseDouble(costStr);
            } catch (Exception e) {
                System.out.println("Invalid cost");
                System.out.println(againName);
            }
        }
    }

    /**
     * Enter a number and validate it
     * @param keyboard
     * @param againMessage
     * @return
     */
    private static int inputNumber(Scanner keyboard, String againMessage) {
        while (true) {
            int data;
            try {
                String input = keyboard.nextLine();
                data = Integer.parseInt(input);
                return data;
            } catch (Exception e) {
                System.out.println("Invalid data, please enter again");
                System.out.println(againMessage);
            }
        }
    }

    /**
     * Enter a date input and validate it
     * @param keyboard
     * @param againMessage
     * @return
     */
    private static Date inputDate(Scanner keyboard, String againMessage) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        while (true) {
            try {
                String date = keyboard.nextLine();
                Date parseDate = new Date(simpleDateFormat.parse(date).getTime());
                return parseDate;
            } catch (Exception e) {
                System.out.println("Invalid format date, please enter again. (dd-MM-yyyy): ");
                System.out.println(againMessage);
            }
        }
    }

    /**
     * Enter a job_number and validate it
     * @param keyboard
     * @param jobList
     * @return
     */
    private static Job inputJob(Scanner keyboard, List<Job> jobList) {
        while (true) {
            System.out.println("Enter the job id: ");
            int jobNumber;
            try {
                jobNumber = Integer.parseInt(keyboard.nextLine());

                Optional<Job> optionalJob = jobList.stream().filter(c -> c.getJobNumber() == jobNumber).findFirst();
                if (!optionalJob.isPresent()) {
                    System.out.println("The job id is not in the list, please enter again");
                    continue;
                }
                return optionalJob.get();
            } catch (Exception e) {
                System.out.println("Invalid job id, please enter again");
            }
        }
    }

    /**
     * Enter a account_number and validate it
     * @param keyboard
     * @param accountList
     * @return
     */
    private static Account inputAccount(Scanner keyboard, List<Account> accountList) {
        while (true) {
            System.out.println("Enter the account number: ");
            String accountNumber;
            try {
                accountNumber = keyboard.nextLine();

                Optional<Account> optionalAccount = accountList.stream().filter(c -> accountNumber.equals(c.getAccountNumber())).findFirst();
                if (!optionalAccount.isPresent()) {
                    System.out.println("The account number is not in the list, please enter again");
                    continue;
                }
                return optionalAccount.get();
            } catch (Exception e) {
                System.out.println("Invalid account number, please enter again");
            }
        }
    }

    /**
     * Print the assembly data
     * @param assemblyList
     */
    private static void printAssemblyData(List<Assembly> assemblyList) {
        System.out.println("The list assembly: ");
        System.out.println("Assembly_Id---Assembly_Detail");
        for (Assembly assembly : assemblyList) {
            System.out.println(assembly.getAssemblyId() + "---" + assembly.getAssemblyDetail());
        }
    }

    /**
     * Enter a transaction_id and validate it
     * @param keyboard
     * @param transactionList
     * @return
     */
    private static Transaction inputTransaction(Scanner keyboard, List<Transaction> transactionList) {
        while (true) {
            System.out.println("Enter the transaction number: ");
            int transactionNumber;
            try {
                transactionNumber = Integer.parseInt(keyboard.nextLine());

                Optional<Transaction> optionalTransaction = transactionList.stream().filter(c -> c.getTransactionNumber() == transactionNumber).findFirst();
                if (!optionalTransaction.isPresent()) {
                    System.out.println("The transaction number is not in the list, please enter again");
                    continue;
                }
                return optionalTransaction.get();
            } catch (Exception e) {
                System.out.println("Invalid transaction number, please enter again");
            }
        }
    }

    /**
     * Enter a assembly_id and validate it
     * @param keyboard
     * @param assemblyList
     * @return
     */
    private static Assembly inputAssembly(Scanner keyboard, List<Assembly> assemblyList) {
        while (true) {
            System.out.println("Enter the assembly id: ");
            int assemblyId;
            try {
                assemblyId = Integer.parseInt(keyboard.nextLine());

                Optional<Assembly> optionalAssembly = assemblyList.stream().filter(c -> c.getAssemblyId() == assemblyId).findFirst();
                if (!optionalAssembly.isPresent()) {
                    System.out.println("The assembly id is not in the list, please enter again");
                    continue;
                }
                return optionalAssembly.get();
            } catch (Exception e) {
                System.out.println("Invalid assembly id, please enter again");
            }
        }
    }

    /**
     * Enter a process_id and validate it
     * @param keyboard
     * @param processList
     * @return
     */
    private static Process inputProcess(Scanner keyboard, List<Process> processList) {
        while (true) {
            System.out.println("Enter a process id: ");
            int processId;
            try {
                processId = Integer.parseInt(keyboard.nextLine());

                Optional<Process> optionalProcess = processList.stream().filter(c -> c.getProcessId() == processId).findFirst();
                if (!optionalProcess.isPresent()) {
                    System.out.println("The process id is not in the list, please enter again");
                    continue;
                }
                return optionalProcess.get();
            } catch (Exception e) {
                System.out.println("Invalid process id, please enter again");
            }
        }
    }

    /**
     * Enter customer_id and validate it
     * @param keyboard
     * @param customerList
     * @return
     */
    private static Customer inputCustomer(Scanner keyboard, List<Customer> customerList) {
        while (true) {
            System.out.println("Enter a customer id: ");
            int customerId;
            try {
                customerId = Integer.parseInt(keyboard.nextLine());

                Optional<Customer> optionalCustomer = customerList.stream().filter(c -> c.getCustomerId() == customerId).findFirst();
                if (!optionalCustomer.isPresent()) {
                    System.out.println("The customer id is not in the list, please enter again");
                    continue;
                }
                return optionalCustomer.get();
            } catch (Exception e) {
                System.out.println("Invalid customer id, please enter again");
            }
        }
    }

    /**
     * Enter department_id and validate it
     * @param keyboard
     * @param departmentList
     * @return
     */
    private static Department inputDepartment(Scanner keyboard, List<Department> departmentList) {
        while (true) {
            System.out.println("Enter the department id: ");
            int departmentId;
            try {
                departmentId = Integer.parseInt(keyboard.nextLine());

                Optional<Department> optDepartment = departmentList.stream().filter(d -> d.getDepartmentId() == departmentId).findFirst();
                if (!optDepartment.isPresent()) {
                    System.out.println("The department id is not in the list, please enter again");
                    continue;
                }
                return optDepartment.get();
            } catch (Exception e) {
                System.out.println("Invalid department id, please enter again");
            }
        }
    }

    /**
     * Print account data
     * @param accountList
     */
    private static void printAccountData(List<Account> accountList) {
        System.out.println("The list account: ");
        System.out.println("Account_Number---Account_Type");
        for (Account account : accountList) {
            String accountType;
            if (account instanceof ProcessAccount) {
                accountType = "Process Account";
            } else if (account instanceof DepartmentAccount) {
                accountType = "Department Account";
            } else {
                accountType = "Assembly Account";
            }

            System.out.println(account.getAccountNumber() + "---" + accountType);
        }
    }

    /**
     * Print the job data
     * @param jobList
     */
    private static void printJobData(List<Job> jobList) {
        System.out.println("The list job: ");
        System.out.println("Job_Number---Job_Type");
        for (Job job : jobList) {
            String jobType;
            if (job instanceof FitJob) {
                jobType = "Fit Job";
            } else if (job instanceof CutJob) {
                jobType = "Cut Job";
            } else {
                jobType = "Paint Job";
            }

            System.out.println(job.getJobNumber() + "---" + jobType);
        }
    }

    /**
     * Print the process data
     * @param processList
     */
    private static void printProcessData(List<Process> processList) {
        System.out.println("The list process: ");
        System.out.println("Process_ID---Process_Type---Process_Data");
        for (Process process : processList) {
            String processType;
            if (process instanceof FitProcess) {
                processType = "Fit Process";
            } else if (process instanceof CutProcess) {
                processType = "Cut Process";
            } else {
                processType = "Paint Process";
            }

            System.out.println(process.getProcessId() + "---" + processType + "---" + process.getProcessData());
        }
    }

    /**
     * Print the customer data
     * @param customerList
     */
    private static void printCustomerData(List<Customer> customerList) {
        System.out.println("The list customer: ");
        System.out.println("Customer_Id---Customer_Name---Customer_Address---Category");
        for (Customer customer : customerList) {
            System.out.println(customer.getCustomerId() + "---" + customer.getName() + "---" + customer.getAddress() + "---" + customer.getCategory());
        }
    }

    /**
     * Print the transaction data
     * @param transactionList
     */
    private static void printTransactionData(List<Transaction> transactionList) {
        System.out.println("The list transaction: ");
        System.out.println("Transaction_Id---Supplied_Cost");
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getTransactionNumber() + "---" + transaction.getSuppliedCost());
        }
    }

    /**
     * Print the department data
     * @param departmentList
     */
    private static void printDepartmentData(List<Department> departmentList) {
        System.out.println("The list department: ");
        System.out.println("Department_Id--------------------Department_Data");
        for (Department department : departmentList) {
            System.out.println(department.getDepartmentId() + "---" + department.getDepartmentData());
        }
    }
}
