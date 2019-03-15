package info.pratham.asersample.interfaces;

public interface CheckQuestionListener {
    public void showChekingDialog(String subject,String level,String flag);

    public void onSubmitListener(String subject,boolean changeTab,String calledFrom);
}
