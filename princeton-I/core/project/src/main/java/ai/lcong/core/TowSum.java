import java.util.Arrays;

class TwoSum {
  public int[] twoSum(int[] nums, int target) {
    int n = nums.length;
    int[] nums1 = Arrays.copyOf(nums, n); 
    int[] nums2 = new int[n];
    for (int i = 0; i < n; i++) {
      nums2[i] = target - nums[i];
    }
    Arrays.sort(nums1);
    Arrays.sort(nums2);

    int j1 = 0;
    int j2 = 0;
    boolean found = false;
    while (!found && j1 < n && j2 < n) {
      if (nums1[j1] == nums2[j2]) {
        found = true;
      } else if (nums1[j1] < nums2[j2]) {
        j1++;
      } else {
        j2++;
      }
    }    

    int[] ret = new int[2];
    for (int i = 0; i < n; i++) {
      if (nums[i] == nums1[j1]) ret[0] = i;
      else if (nums[i] == (target-nums1[j1])) ret[1] = i;
      else continue;
    }
    return ret;
  }

  public static void main(String[] args) {
    int[] ret = new TwoSum().twoSum(new int[] { 2, 7, 11, 15 }, 9);
    System.out.println("[" + ret[0] + ", " + ret[1] + "]");
  }
}