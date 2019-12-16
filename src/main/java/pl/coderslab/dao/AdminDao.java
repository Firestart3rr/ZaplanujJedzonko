package pl.coderslab.dao;

import pl.coderslab.model.Admin;
import pl.coderslab.model.SuperAdmin;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    private static final String CREATE_ADMIN_QUERY = "INSERT INTO admins (first_name, last_name, email, password, " +
            "superadmin, enable) VALUES (?,?,?,?,0,1);";
    private static final String DELETE_ADMIN_QUERY = "DELETE FROM admins where id=?;";
    private static final String GET_ADMIN_QUERY = "SELECT id, first_name, last_name, email, password, superadmin, enable FROM admins WHERE id =?;";
    private static final String UPDATE_ADMIN_QUERY = "UPDATE admins SET first_name=?,last_name=?,email=?,password=?;";

    public Admin create(Admin admin) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ADMIN_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, admin.getFirstName());
            preparedStatement.setString(2, admin.getLastName());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                admin.setId(resultSet.getInt(1));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie znaleziono bazy");
        }
        return null;
    }

    public void delete(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ADMIN_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie znaleziono bazy");
        }
    }

    public Admin get(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ADMIN_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Admin admin = new Admin();
            if (resultSet.next()) {
                SuperAdmin superAdmin = new SuperAdmin();
                admin.setId(resultSet.getInt(1));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setEmail(resultSet.getString("email"));
                admin.setPassword(resultSet.getString("password"));
                superAdmin.setEnable(admin, resultSet.getByte("enable"));
                if (resultSet.getByte("superadmin") == 1) {
                    return superAdmin.setSuperAdmin(admin, (byte) 1);
                }
                if (resultSet.getByte("superadmin") == 0) {
                    superAdmin.setSuperAdmin(admin, (byte) 0);
                    return admin;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie znaleziono bazy");
        }
        return null;
    }

    public void update(Admin admin, int id) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ADMIN_QUERY);
            preparedStatement.setString(1, admin.getFirstName());
            preparedStatement.setString(2, admin.getLastName());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie znaleziono bazy");
        }
    }

}