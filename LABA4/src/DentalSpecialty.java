public enum DentalSpecialty {
    ТЕРАПЕВТ("Терапевт", "Кариес", "Пульпит", "Периодонтит"),
    ХИРУРГ("Хирург", "Киста", "Перелом челюсти", "Абсцесс"),
    ОРТОДОНТ("Ортодонт", "Прикус", "Скученность зубов", "Диастема"),
    ПАРОДОНТОЛОГ("Пародонтолог", "Гингивит", "Пародонтит", "Пародонтоз");

    private final String title;
    private final String[] diseases;

    DentalSpecialty(String title, String... diseases) {
        this.title = title;
        this.diseases = diseases;
    }

    public String getTitle() {
        return title;
    }

    public String[] getDiseases() {
        return diseases;
    }

    public static boolean canTreat(String specialty, String disease) {
        for (DentalSpecialty ds : values()) {
            if (ds.getTitle().equals(specialty)) {
                for (String d : ds.getDiseases()) {
                    if (d.equals(disease)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}