package com.elantsev.netology.diplomacloud.dto;

public class NewFileName {
    private String filename;

    public NewFileName(String filename) {
        this.filename = filename;
    }

    public NewFileName() {
    }

    public static NewFileNameBuilder builder() {
        return new NewFileNameBuilder();
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NewFileName)) return false;
        final NewFileName other = (NewFileName) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$filename = this.getFilename();
        final Object other$filename = other.getFilename();
        if (this$filename == null ? other$filename != null : !this$filename.equals(other$filename)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NewFileName;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $filename = this.getFilename();
        result = result * PRIME + ($filename == null ? 43 : $filename.hashCode());
        return result;
    }

    public String toString() {
        return "NewFileName(filename=" + this.getFilename() + ")";
    }

    public static class NewFileNameBuilder {
        private String filename;

        NewFileNameBuilder() {
        }

        public NewFileNameBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public NewFileName build() {
            return new NewFileName(filename);
        }

        public String toString() {
            return "NewFileName.NewFileNameBuilder(filename=" + this.filename + ")";
        }
    }
}
