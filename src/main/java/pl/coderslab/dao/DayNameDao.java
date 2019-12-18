package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.DayName;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DayNameDao {
    // ZAPYTANIA SQL
    private static final String CREATE_DAY_NAME_QUERY = "INSERT INTO day_name(name, display_order) VALUES (?,?);";
    private static final String DELETE_DAY_NAME_QUERY = "DELETE FROM day_name where id = ?;";
    private static final String FIND_ALL_DAY_NAMES_QUERY = "SELECT * FROM day_name;";
    private static final String READ_DAY_NAME_QUERY = "SELECT * from day_name where id = ?;";
    private static final String UPDATE_DAY_NAME_QUERY = "UPDATE	day_name SET name = ? , display_order = ? WHERE	id = ?;";

    /**
     * Get day_name by id
     *
     * @param dayNameId
     * @return
     */
    public DayName read(Integer dayNameId) {
        DayName dayName = new DayName();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_DAY_NAME_QUERY)
        ) {
            statement.setInt(1, dayNameId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dayName.setId(resultSet.getInt("id"));
                    dayName.setName(resultSet.getString("name"));
                    dayName.setDisplayOrder(resultSet.getInt("display_order"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayName;

    }

    /**
     * Return all books
     *
     * @return
     */
    public List<DayName> findAll() {
        List<DayName> dayNameList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_DAY_NAMES_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                DayName dayToAdd = new DayName();
                dayToAdd.setId(resultSet.getInt("id"));
                dayToAdd.setName(resultSet.getString("name"));
                dayToAdd.setDisplayOrder(resultSet.getInt("display_order"));
                dayNameList.add(dayToAdd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dayNameList;

    }

    /**
     * Create book
     *
     * @param dayName
     * @return
     */
    public static DayName create(DayName dayName) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement(CREATE_DAY_NAME_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStm.setString(1, dayName.getName());
            insertStm.setInt(2, dayName.getDisplayOrder());
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    dayName.setId(generatedKeys.getInt(1));
                    return dayName;
                } else {
                    throw new RuntimeException("Generated key was not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Remove book by id
     *
     * @param dayNameId
     */
    public void delete(Integer dayNameId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DAY_NAME_QUERY)) {
            statement.setInt(1, dayNameId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Update book
     *
     * @param dayName
     */
    public void update(DayName dayName) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DAY_NAME_QUERY)) {
            statement.setInt(3, dayName.getId());
            statement.setString(1, dayName.getName());
            statement.setInt(2, dayName.getDisplayOrder());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getDayIdByName(String dayName) {
        List<DayName> daynames = findAll();
        int dayId = 0;
        for (DayName day : daynames) {
            if (dayName.equals(day.getName())) {
                dayId = day.getId();
            }
        }
        return dayId;
    }
}
