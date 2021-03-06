package dev.abelab.crms.db.mapper;

import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    long countByExample(ReservationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int deleteByExample(ReservationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int insert(Reservation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int insertSelective(Reservation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    List<Reservation> selectByExample(ReservationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    Reservation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Reservation record, @Param("example") ReservationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Reservation record, @Param("example") ReservationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Reservation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reservation
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Reservation record);
}