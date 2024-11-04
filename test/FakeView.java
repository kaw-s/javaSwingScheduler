import view.Features;
import view.IView;

/**
 * Represents a fake view, which contains bare-bones implementations of
 * the methods defined in an IView. The purpose of this class is to determine
 * whether the controller correctly delegated information back to the view.
 */
public class FakeView implements IView {
  private final StringBuilder stringBuilder;

  public FakeView(StringBuilder stringBuilder) {
    this.stringBuilder = stringBuilder;
  }

  @Override
  public void addFeatures(Features features) {
    stringBuilder.append("features");
  }

  @Override
  public void refresh() {
    this.stringBuilder.append("refreshed");
  }

  @Override
  public void makeVisible() {
    System.out.println("Making visible");
  }
}
