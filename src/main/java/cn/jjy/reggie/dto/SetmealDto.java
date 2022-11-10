package cn.jjy.reggie.dto;
import cn.jjy.reggie.entity.Setmeal;
import cn.jjy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
