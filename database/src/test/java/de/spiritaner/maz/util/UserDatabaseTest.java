package de.spiritaner.maz.util;

import de.spiritaner.maz.util.database.UserDatabase;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Florian Schwab
 * @version 0.0.1
 */
public class UserDatabaseTest {
	private static final String DATABASE_PATH = "./dbfiles/";
	private static final String USER_DATABASE_NAME = "users";
	private static final String USER_PASSWORD_TABLE_NAME = "USER_PASSWORD";

	@BeforeTest
	public void setup(ITestContext ctx) {
		TestRunner runner = (TestRunner) ctx;
		runner.setOutputDirectory("./target/test/");
	}

	@BeforeMethod
	public void removeDatabaseFile() throws Exception {
		File usersDbFile = new File("./dbfiles/users.mv.db");

		if(usersDbFile.exists()) {
			usersDbFile.delete();
			usersDbFile.getParentFile().delete();
		}
	}

	@Test(enabled = false)
	public void testIsPopulatedWithoutTable() throws Exception {
		assert !UserDatabase.isPopulated();
	}

	@Test(enabled = false)
	public void testIsPopulatedWithoutEntries() throws Exception {
		try (Connection conn = getConnection()) {
			Statement st = conn.createStatement();
			st.executeUpdate("CREATE TABLE "+USER_PASSWORD_TABLE_NAME+" (ID int, username varchar(64));");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		assert !UserDatabase.isPopulated();
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		return DriverManager.getConnection("jdbc:h2:"+DATABASE_PATH+USER_DATABASE_NAME, "anonymous", "");
	}
}