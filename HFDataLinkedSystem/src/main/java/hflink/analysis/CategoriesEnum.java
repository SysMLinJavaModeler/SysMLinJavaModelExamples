package hflink.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for the categories of wait time intervals
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings("javadoc")
public enum CategoriesEnum
{
	Cat0to4(0, 5, "0-5"), Cat5to9(5, 10, "5-10"), Cat10to14(10, 15, "10-15"), Cat15to19(15, 20, "15-20"), Cat20to24(20, 25, "20-25"), Cat25to29(25, 30, "25-30"), Cat30to34(30, 35, "30-35"), Cat35to39(35, 40, "35-40"), Cat40to44(40, 45,
	"40-45"), Cat45to49(45, 50, "45-50"), Cat50to54(50, 55, "50-55"), Cat55to59(55, 60, "55-60"), Cat60toN(60, Double.MAX_VALUE, "60+");

	/**
	 * Category's min wait time
	 */
	double minWaitTimeSeconds;
	/**
	 * Category's max wait time
	 */
	double maxWaitTimeSeconds;
	/**
	 * Name of category
	 */
	String catName;

	/**
	 * Private constructor
	 * 
	 * @param minWaitTimeSeconds min wait time in category
	 * @param maxWaitTimeSeconds max wait time in category
	 * @param catName            category name
	 */
	private CategoriesEnum(double minWaitTimeSeconds, double maxWaitTimeSeconds, String catName)
	{
		this.minWaitTimeSeconds = minWaitTimeSeconds;
		this.maxWaitTimeSeconds = maxWaitTimeSeconds;
		this.catName = catName;
	}

	/**
	 * Returns list of category names
	 * 
	 * @return list of names
	 */
	public static List<String> namesList()
	{
		List<String> result = new ArrayList<>();
		for (CategoriesEnum cat : values())
			result.add(cat.catName);
		return result;
	}
}