package com.rao.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class ForFilterUtil {

    private static final String URL = "jdbc:mysql://101.200.15.215:3306/item-management";
    private static final String USER = "root";
    private static final String PASSWORD = "fly142857";

    public static Map<String, Integer> getUserRole(String userId) {
        String sql = "SELECT role, version FROM user WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 设置查询条件
            statement.setString(1, userId);
            Map<String, Integer> result = new HashMap<>();
            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result.put("role", resultSet.getInt("role")); // 获取用户权限
                    result.put("version", resultSet.getInt("version"));
                    return result;
                }
            } catch (SQLException e) {
                log.error("查询出错：{}", e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            log.error("连接数据库失败：{}", e.getMessage());
            return null;
        }

        return null; // 用户权限不存在

    }
}
