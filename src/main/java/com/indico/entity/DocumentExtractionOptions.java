package com.indico.entity;

public class DocumentExtractionOptions {

    public final Boolean singleColumn;
    public final Boolean text;
    public final Boolean rawText;
    public final Boolean tables;
    public final Boolean metadata;
    public final Boolean forceRender;
    public final Boolean detailed;

    /**
     * Class Constructor
     *
     * @param singleColumn when True, assumes the input is a single column of
     * text.
     * @param text when True, returns body text as part of the results for each
     * page.
     * @param rawText when True, returns all body text for the PDF document in a
     * single block. This text matches what you would see in Indico Teach.
     * @param tables when True, returns the contents of tables in the document,
     * separately from body text
     * @param metadata when True, returns the following: tagged, form, producer,
     * author, encryption status, program used to create the file, file size,
     * PDF version, optimized, modification date, title, creation date, number
     * of pages, page size.
     * @param forceRender Force rendering to PNG instead of using text from
     * native PDF.
     * @param detailed Include detailed positional information.
     */
    private DocumentExtractionOptions(Builder builder) {
        this.singleColumn = builder.singleColumn;
        this.text = builder.text;
        this.rawText = builder.rawText;
        this.tables = builder.tables;
        this.metadata = builder.metadata;
        this.forceRender = builder.forceRender;
        this.detailed = builder.detailed;
    }

    public static class Builder {

        protected Boolean singleColumn = false;
        protected Boolean text = false;
        protected Boolean rawText = false;
        protected Boolean tables = false;
        protected Boolean metadata = false;
        protected Boolean forceRender = false;
        protected Boolean detailed = false;

        /**
         * assumes the input is a single column of text.
         *
         * @param singleColumn defaults to false
         * @return Builder instance
         */
        public Builder singleColumn(Boolean singleColumn) {
            this.singleColumn = singleColumn;
            return this;
        }

        /**
         * returns body text as part of the results for each page.
         *
         * @param text defaults to false
         * @return Builder instance
         */
        public Builder text(Boolean text) {
            this.text = text;
            return this;
        }

        /**
         * returns all body text for the PDF document in a single block.This
         * text matches what you would see in Indico Teach.
         *
         * @param rawText defaults to false
         * @return Builder instance
         */
        public Builder rawText(Boolean rawText) {
            this.rawText = rawText;
            return this;
        }

        /**
         * returns the contents of tables in the document, separately from body
         * text
         *
         * @param tables defaults to false
         * @return Builder instance
         */
        public Builder tables(boolean tables) {
            this.tables = tables;
            return this;
        }

        /**
         * returns the following: tagged, form, producer, author, encryption
         * status, program used to create the file, file size, PDF version,
         * optimized, modification date, title, creation date, number of pages,
         * page size
         *
         * @param metadata defaults to false
         * @return Builder instance
         */
        public Builder metadata(Boolean metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Force rendering to PNG instead of using text from native PDF
         *
         * @param forceRender defaults to false
         * @return Builder instance
         */
        public Builder forceRender(Boolean forceRender) {
            this.forceRender = forceRender;
            return this;
        }

        /**
         * Include detailed positional information
         *
         * @param detailed defaults to false
         * @return Builder instance
         */
        public Builder detailed(Boolean detailed) {
            this.detailed = detailed;
            return this;
        }

        /**
         * Returns instance of DocumentExtractionOptions to pass to
         * Indico.DocumentExtraction
         *
         * @return instance of DocumentExtractionOptions
         */
        public DocumentExtractionOptions build() {
            return new DocumentExtractionOptions(this);
        }
    }
}
